import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMuscle2Exercise, getMuscle2ExerciseIdentifier } from '../muscle-2-exercise.model';

export type EntityResponseType = HttpResponse<IMuscle2Exercise>;
export type EntityArrayResponseType = HttpResponse<IMuscle2Exercise[]>;

@Injectable({ providedIn: 'root' })
export class Muscle2ExerciseService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/muscle-2-exercises');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(muscle2Exercise: IMuscle2Exercise): Observable<EntityResponseType> {
    return this.http.post<IMuscle2Exercise>(this.resourceUrl, muscle2Exercise, { observe: 'response' });
  }

  update(muscle2Exercise: IMuscle2Exercise): Observable<EntityResponseType> {
    return this.http.put<IMuscle2Exercise>(
      `${this.resourceUrl}/${getMuscle2ExerciseIdentifier(muscle2Exercise) as number}`,
      muscle2Exercise,
      { observe: 'response' }
    );
  }

  partialUpdate(muscle2Exercise: IMuscle2Exercise): Observable<EntityResponseType> {
    return this.http.patch<IMuscle2Exercise>(
      `${this.resourceUrl}/${getMuscle2ExerciseIdentifier(muscle2Exercise) as number}`,
      muscle2Exercise,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMuscle2Exercise>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMuscle2Exercise[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMuscle2ExerciseToCollectionIfMissing(
    muscle2ExerciseCollection: IMuscle2Exercise[],
    ...muscle2ExercisesToCheck: (IMuscle2Exercise | null | undefined)[]
  ): IMuscle2Exercise[] {
    const muscle2Exercises: IMuscle2Exercise[] = muscle2ExercisesToCheck.filter(isPresent);
    if (muscle2Exercises.length > 0) {
      const muscle2ExerciseCollectionIdentifiers = muscle2ExerciseCollection.map(
        muscle2ExerciseItem => getMuscle2ExerciseIdentifier(muscle2ExerciseItem)!
      );
      const muscle2ExercisesToAdd = muscle2Exercises.filter(muscle2ExerciseItem => {
        const muscle2ExerciseIdentifier = getMuscle2ExerciseIdentifier(muscle2ExerciseItem);
        if (muscle2ExerciseIdentifier == null || muscle2ExerciseCollectionIdentifiers.includes(muscle2ExerciseIdentifier)) {
          return false;
        }
        muscle2ExerciseCollectionIdentifiers.push(muscle2ExerciseIdentifier);
        return true;
      });
      return [...muscle2ExercisesToAdd, ...muscle2ExerciseCollection];
    }
    return muscle2ExerciseCollection;
  }
}
