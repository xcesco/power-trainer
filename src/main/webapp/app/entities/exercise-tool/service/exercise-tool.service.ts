import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IExerciseTool, getExerciseToolIdentifier } from '../exercise-tool.model';

export type EntityResponseType = HttpResponse<IExerciseTool>;
export type EntityArrayResponseType = HttpResponse<IExerciseTool[]>;

@Injectable({ providedIn: 'root' })
export class ExerciseToolService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/exercise-tools');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(exerciseTool: IExerciseTool): Observable<EntityResponseType> {
    return this.http.post<IExerciseTool>(this.resourceUrl, exerciseTool, { observe: 'response' });
  }

  update(exerciseTool: IExerciseTool): Observable<EntityResponseType> {
    return this.http.put<IExerciseTool>(`${this.resourceUrl}/${getExerciseToolIdentifier(exerciseTool) as number}`, exerciseTool, {
      observe: 'response',
    });
  }

  partialUpdate(exerciseTool: IExerciseTool): Observable<EntityResponseType> {
    return this.http.patch<IExerciseTool>(`${this.resourceUrl}/${getExerciseToolIdentifier(exerciseTool) as number}`, exerciseTool, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IExerciseTool>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IExerciseTool[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addExerciseToolToCollectionIfMissing(
    exerciseToolCollection: IExerciseTool[],
    ...exerciseToolsToCheck: (IExerciseTool | null | undefined)[]
  ): IExerciseTool[] {
    const exerciseTools: IExerciseTool[] = exerciseToolsToCheck.filter(isPresent);
    if (exerciseTools.length > 0) {
      const exerciseToolCollectionIdentifiers = exerciseToolCollection.map(
        exerciseToolItem => getExerciseToolIdentifier(exerciseToolItem)!
      );
      const exerciseToolsToAdd = exerciseTools.filter(exerciseToolItem => {
        const exerciseToolIdentifier = getExerciseToolIdentifier(exerciseToolItem);
        if (exerciseToolIdentifier == null || exerciseToolCollectionIdentifiers.includes(exerciseToolIdentifier)) {
          return false;
        }
        exerciseToolCollectionIdentifiers.push(exerciseToolIdentifier);
        return true;
      });
      return [...exerciseToolsToAdd, ...exerciseToolCollection];
    }
    return exerciseToolCollection;
  }
}
