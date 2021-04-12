import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWorkout, getWorkoutIdentifier } from '../workout.model';

export type EntityResponseType = HttpResponse<IWorkout>;
export type EntityArrayResponseType = HttpResponse<IWorkout[]>;

@Injectable({ providedIn: 'root' })
export class WorkoutService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/workouts');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(workout: IWorkout): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(workout);
    return this.http
      .post<IWorkout>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(workout: IWorkout): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(workout);
    return this.http
      .put<IWorkout>(`${this.resourceUrl}/${getWorkoutIdentifier(workout) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(workout: IWorkout): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(workout);
    return this.http
      .patch<IWorkout>(`${this.resourceUrl}/${getWorkoutIdentifier(workout) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IWorkout>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IWorkout[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addWorkoutToCollectionIfMissing(workoutCollection: IWorkout[], ...workoutsToCheck: (IWorkout | null | undefined)[]): IWorkout[] {
    const workouts: IWorkout[] = workoutsToCheck.filter(isPresent);
    if (workouts.length > 0) {
      const workoutCollectionIdentifiers = workoutCollection.map(workoutItem => getWorkoutIdentifier(workoutItem)!);
      const workoutsToAdd = workouts.filter(workoutItem => {
        const workoutIdentifier = getWorkoutIdentifier(workoutItem);
        if (workoutIdentifier == null || workoutCollectionIdentifiers.includes(workoutIdentifier)) {
          return false;
        }
        workoutCollectionIdentifiers.push(workoutIdentifier);
        return true;
      });
      return [...workoutsToAdd, ...workoutCollection];
    }
    return workoutCollection;
  }

  protected convertDateFromClient(workout: IWorkout): IWorkout {
    return Object.assign({}, workout, {
      date: workout.date?.isValid() ? workout.date.toJSON() : undefined,
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
      res.body.forEach((workout: IWorkout) => {
        workout.date = workout.date ? dayjs(workout.date) : undefined;
      });
    }
    return res;
  }
}
