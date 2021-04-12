import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IExerciseValue, getExerciseValueIdentifier } from '../exercise-value.model';

export type EntityResponseType = HttpResponse<IExerciseValue>;
export type EntityArrayResponseType = HttpResponse<IExerciseValue[]>;

@Injectable({ providedIn: 'root' })
export class ExerciseValueService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/exercise-values');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(exerciseValue: IExerciseValue): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(exerciseValue);
    return this.http
      .post<IExerciseValue>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(exerciseValue: IExerciseValue): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(exerciseValue);
    return this.http
      .put<IExerciseValue>(`${this.resourceUrl}/${getExerciseValueIdentifier(exerciseValue) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(exerciseValue: IExerciseValue): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(exerciseValue);
    return this.http
      .patch<IExerciseValue>(`${this.resourceUrl}/${getExerciseValueIdentifier(exerciseValue) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IExerciseValue>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IExerciseValue[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addExerciseValueToCollectionIfMissing(
    exerciseValueCollection: IExerciseValue[],
    ...exerciseValuesToCheck: (IExerciseValue | null | undefined)[]
  ): IExerciseValue[] {
    const exerciseValues: IExerciseValue[] = exerciseValuesToCheck.filter(isPresent);
    if (exerciseValues.length > 0) {
      const exerciseValueCollectionIdentifiers = exerciseValueCollection.map(
        exerciseValueItem => getExerciseValueIdentifier(exerciseValueItem)!
      );
      const exerciseValuesToAdd = exerciseValues.filter(exerciseValueItem => {
        const exerciseValueIdentifier = getExerciseValueIdentifier(exerciseValueItem);
        if (exerciseValueIdentifier == null || exerciseValueCollectionIdentifiers.includes(exerciseValueIdentifier)) {
          return false;
        }
        exerciseValueCollectionIdentifiers.push(exerciseValueIdentifier);
        return true;
      });
      return [...exerciseValuesToAdd, ...exerciseValueCollection];
    }
    return exerciseValueCollection;
  }

  protected convertDateFromClient(exerciseValue: IExerciseValue): IExerciseValue {
    return Object.assign({}, exerciseValue, {
      date: exerciseValue.date?.isValid() ? exerciseValue.date.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? dayjs(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((exerciseValue: IExerciseValue) => {
        exerciseValue.date = exerciseValue.date ? dayjs(exerciseValue.date) : undefined;
      });
    }
    return res;
  }
}
