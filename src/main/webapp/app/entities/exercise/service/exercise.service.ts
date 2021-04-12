import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IExercise, getExerciseIdentifier } from '../exercise.model';

export type EntityResponseType = HttpResponse<IExercise>;
export type EntityArrayResponseType = HttpResponse<IExercise[]>;

@Injectable({ providedIn: 'root' })
export class ExerciseService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/exercises');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(exercise: IExercise): Observable<EntityResponseType> {
    return this.http.post<IExercise>(this.resourceUrl, exercise, { observe: 'response' });
  }

  update(exercise: IExercise): Observable<EntityResponseType> {
    return this.http.put<IExercise>(`${this.resourceUrl}/${getExerciseIdentifier(exercise) as number}`, exercise, { observe: 'response' });
  }

  partialUpdate(exercise: IExercise): Observable<EntityResponseType> {
    return this.http.patch<IExercise>(`${this.resourceUrl}/${getExerciseIdentifier(exercise) as number}`, exercise, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IExercise>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IExercise[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addExerciseToCollectionIfMissing(exerciseCollection: IExercise[], ...exercisesToCheck: (IExercise | null | undefined)[]): IExercise[] {
    const exercises: IExercise[] = exercisesToCheck.filter(isPresent);
    if (exercises.length > 0) {
      const exerciseCollectionIdentifiers = exerciseCollection.map(exerciseItem => getExerciseIdentifier(exerciseItem)!);
      const exercisesToAdd = exercises.filter(exerciseItem => {
        const exerciseIdentifier = getExerciseIdentifier(exerciseItem);
        if (exerciseIdentifier == null || exerciseCollectionIdentifiers.includes(exerciseIdentifier)) {
          return false;
        }
        exerciseCollectionIdentifiers.push(exerciseIdentifier);
        return true;
      });
      return [...exercisesToAdd, ...exerciseCollection];
    }
    return exerciseCollection;
  }
}
