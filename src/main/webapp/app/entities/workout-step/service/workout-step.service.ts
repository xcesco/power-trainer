import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWorkoutStep, getWorkoutStepIdentifier } from '../workout-step.model';

export type EntityResponseType = HttpResponse<IWorkoutStep>;
export type EntityArrayResponseType = HttpResponse<IWorkoutStep[]>;

@Injectable({ providedIn: 'root' })
export class WorkoutStepService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/workout-steps');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(workoutStep: IWorkoutStep): Observable<EntityResponseType> {
    return this.http.post<IWorkoutStep>(this.resourceUrl, workoutStep, { observe: 'response' });
  }

  update(workoutStep: IWorkoutStep): Observable<EntityResponseType> {
    return this.http.put<IWorkoutStep>(`${this.resourceUrl}/${getWorkoutStepIdentifier(workoutStep) as number}`, workoutStep, {
      observe: 'response',
    });
  }

  partialUpdate(workoutStep: IWorkoutStep): Observable<EntityResponseType> {
    return this.http.patch<IWorkoutStep>(`${this.resourceUrl}/${getWorkoutStepIdentifier(workoutStep) as number}`, workoutStep, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IWorkoutStep>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWorkoutStep[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addWorkoutStepToCollectionIfMissing(
    workoutStepCollection: IWorkoutStep[],
    ...workoutStepsToCheck: (IWorkoutStep | null | undefined)[]
  ): IWorkoutStep[] {
    const workoutSteps: IWorkoutStep[] = workoutStepsToCheck.filter(isPresent);
    if (workoutSteps.length > 0) {
      const workoutStepCollectionIdentifiers = workoutStepCollection.map(workoutStepItem => getWorkoutStepIdentifier(workoutStepItem)!);
      const workoutStepsToAdd = workoutSteps.filter(workoutStepItem => {
        const workoutStepIdentifier = getWorkoutStepIdentifier(workoutStepItem);
        if (workoutStepIdentifier == null || workoutStepCollectionIdentifiers.includes(workoutStepIdentifier)) {
          return false;
        }
        workoutStepCollectionIdentifiers.push(workoutStepIdentifier);
        return true;
      });
      return [...workoutStepsToAdd, ...workoutStepCollection];
    }
    return workoutStepCollection;
  }
}
