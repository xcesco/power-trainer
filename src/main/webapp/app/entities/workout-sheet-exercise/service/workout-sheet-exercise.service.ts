import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWorkoutSheetExercise, getWorkoutSheetExerciseIdentifier } from '../workout-sheet-exercise.model';

export type EntityResponseType = HttpResponse<IWorkoutSheetExercise>;
export type EntityArrayResponseType = HttpResponse<IWorkoutSheetExercise[]>;

@Injectable({ providedIn: 'root' })
export class WorkoutSheetExerciseService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/workout-sheet-exercises');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(workoutSheetExercise: IWorkoutSheetExercise): Observable<EntityResponseType> {
    return this.http.post<IWorkoutSheetExercise>(this.resourceUrl, workoutSheetExercise, { observe: 'response' });
  }

  update(workoutSheetExercise: IWorkoutSheetExercise): Observable<EntityResponseType> {
    return this.http.put<IWorkoutSheetExercise>(
      `${this.resourceUrl}/${getWorkoutSheetExerciseIdentifier(workoutSheetExercise) as number}`,
      workoutSheetExercise,
      { observe: 'response' }
    );
  }

  partialUpdate(workoutSheetExercise: IWorkoutSheetExercise): Observable<EntityResponseType> {
    return this.http.patch<IWorkoutSheetExercise>(
      `${this.resourceUrl}/${getWorkoutSheetExerciseIdentifier(workoutSheetExercise) as number}`,
      workoutSheetExercise,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IWorkoutSheetExercise>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWorkoutSheetExercise[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addWorkoutSheetExerciseToCollectionIfMissing(
    workoutSheetExerciseCollection: IWorkoutSheetExercise[],
    ...workoutSheetExercisesToCheck: (IWorkoutSheetExercise | null | undefined)[]
  ): IWorkoutSheetExercise[] {
    const workoutSheetExercises: IWorkoutSheetExercise[] = workoutSheetExercisesToCheck.filter(isPresent);
    if (workoutSheetExercises.length > 0) {
      const workoutSheetExerciseCollectionIdentifiers = workoutSheetExerciseCollection.map(
        workoutSheetExerciseItem => getWorkoutSheetExerciseIdentifier(workoutSheetExerciseItem)!
      );
      const workoutSheetExercisesToAdd = workoutSheetExercises.filter(workoutSheetExerciseItem => {
        const workoutSheetExerciseIdentifier = getWorkoutSheetExerciseIdentifier(workoutSheetExerciseItem);
        if (workoutSheetExerciseIdentifier == null || workoutSheetExerciseCollectionIdentifiers.includes(workoutSheetExerciseIdentifier)) {
          return false;
        }
        workoutSheetExerciseCollectionIdentifiers.push(workoutSheetExerciseIdentifier);
        return true;
      });
      return [...workoutSheetExercisesToAdd, ...workoutSheetExerciseCollection];
    }
    return workoutSheetExerciseCollection;
  }
}
