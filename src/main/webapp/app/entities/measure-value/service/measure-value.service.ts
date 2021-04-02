import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMeasureValue, getMeasureValueIdentifier } from '../measure-value.model';

export type EntityResponseType = HttpResponse<IMeasureValue>;
export type EntityArrayResponseType = HttpResponse<IMeasureValue[]>;

@Injectable({ providedIn: 'root' })
export class MeasureValueService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/measure-values');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(measureValue: IMeasureValue): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(measureValue);
    return this.http
      .post<IMeasureValue>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(measureValue: IMeasureValue): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(measureValue);
    return this.http
      .put<IMeasureValue>(`${this.resourceUrl}/${getMeasureValueIdentifier(measureValue) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(measureValue: IMeasureValue): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(measureValue);
    return this.http
      .patch<IMeasureValue>(`${this.resourceUrl}/${getMeasureValueIdentifier(measureValue) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IMeasureValue>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMeasureValue[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMeasureValueToCollectionIfMissing(
    measureValueCollection: IMeasureValue[],
    ...measureValuesToCheck: (IMeasureValue | null | undefined)[]
  ): IMeasureValue[] {
    const measureValues: IMeasureValue[] = measureValuesToCheck.filter(isPresent);
    if (measureValues.length > 0) {
      const measureValueCollectionIdentifiers = measureValueCollection.map(
        measureValueItem => getMeasureValueIdentifier(measureValueItem)!
      );
      const measureValuesToAdd = measureValues.filter(measureValueItem => {
        const measureValueIdentifier = getMeasureValueIdentifier(measureValueItem);
        if (measureValueIdentifier == null || measureValueCollectionIdentifiers.includes(measureValueIdentifier)) {
          return false;
        }
        measureValueCollectionIdentifiers.push(measureValueIdentifier);
        return true;
      });
      return [...measureValuesToAdd, ...measureValueCollection];
    }
    return measureValueCollection;
  }

  protected convertDateFromClient(measureValue: IMeasureValue): IMeasureValue {
    return Object.assign({}, measureValue, {
      date: measureValue.date?.isValid() ? measureValue.date.toJSON() : undefined,
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
      res.body.forEach((measureValue: IMeasureValue) => {
        measureValue.date = measureValue.date ? dayjs(measureValue.date) : undefined;
      });
    }
    return res;
  }
}
