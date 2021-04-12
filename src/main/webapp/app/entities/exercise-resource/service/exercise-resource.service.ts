import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IExerciseResource, getExerciseResourceIdentifier } from '../exercise-resource.model';

export type EntityResponseType = HttpResponse<IExerciseResource>;
export type EntityArrayResponseType = HttpResponse<IExerciseResource[]>;

@Injectable({ providedIn: 'root' })
export class ExerciseResourceService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/exercise-resources');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(exerciseResource: IExerciseResource): Observable<EntityResponseType> {
    return this.http.post<IExerciseResource>(this.resourceUrl, exerciseResource, { observe: 'response' });
  }

  update(exerciseResource: IExerciseResource): Observable<EntityResponseType> {
    return this.http.put<IExerciseResource>(
      `${this.resourceUrl}/${getExerciseResourceIdentifier(exerciseResource) as number}`,
      exerciseResource,
      { observe: 'response' }
    );
  }

  partialUpdate(exerciseResource: IExerciseResource): Observable<EntityResponseType> {
    return this.http.patch<IExerciseResource>(
      `${this.resourceUrl}/${getExerciseResourceIdentifier(exerciseResource) as number}`,
      exerciseResource,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IExerciseResource>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IExerciseResource[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addExerciseResourceToCollectionIfMissing(
    exerciseResourceCollection: IExerciseResource[],
    ...exerciseResourcesToCheck: (IExerciseResource | null | undefined)[]
  ): IExerciseResource[] {
    const exerciseResources: IExerciseResource[] = exerciseResourcesToCheck.filter(isPresent);
    if (exerciseResources.length > 0) {
      const exerciseResourceCollectionIdentifiers = exerciseResourceCollection.map(
        exerciseResourceItem => getExerciseResourceIdentifier(exerciseResourceItem)!
      );
      const exerciseResourcesToAdd = exerciseResources.filter(exerciseResourceItem => {
        const exerciseResourceIdentifier = getExerciseResourceIdentifier(exerciseResourceItem);
        if (exerciseResourceIdentifier == null || exerciseResourceCollectionIdentifiers.includes(exerciseResourceIdentifier)) {
          return false;
        }
        exerciseResourceCollectionIdentifiers.push(exerciseResourceIdentifier);
        return true;
      });
      return [...exerciseResourcesToAdd, ...exerciseResourceCollection];
    }
    return exerciseResourceCollection;
  }
}
