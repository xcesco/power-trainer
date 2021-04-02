import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMuscle, getMuscleIdentifier } from '../muscle.model';

export type EntityResponseType = HttpResponse<IMuscle>;
export type EntityArrayResponseType = HttpResponse<IMuscle[]>;

@Injectable({ providedIn: 'root' })
export class MuscleService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/muscles');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(muscle: IMuscle): Observable<EntityResponseType> {
    return this.http.post<IMuscle>(this.resourceUrl, muscle, { observe: 'response' });
  }

  update(muscle: IMuscle): Observable<EntityResponseType> {
    return this.http.put<IMuscle>(`${this.resourceUrl}/${getMuscleIdentifier(muscle) as number}`, muscle, { observe: 'response' });
  }

  partialUpdate(muscle: IMuscle): Observable<EntityResponseType> {
    return this.http.patch<IMuscle>(`${this.resourceUrl}/${getMuscleIdentifier(muscle) as number}`, muscle, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMuscle>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMuscle[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMuscleToCollectionIfMissing(muscleCollection: IMuscle[], ...musclesToCheck: (IMuscle | null | undefined)[]): IMuscle[] {
    const muscles: IMuscle[] = musclesToCheck.filter(isPresent);
    if (muscles.length > 0) {
      const muscleCollectionIdentifiers = muscleCollection.map(muscleItem => getMuscleIdentifier(muscleItem)!);
      const musclesToAdd = muscles.filter(muscleItem => {
        const muscleIdentifier = getMuscleIdentifier(muscleItem);
        if (muscleIdentifier == null || muscleCollectionIdentifiers.includes(muscleIdentifier)) {
          return false;
        }
        muscleCollectionIdentifiers.push(muscleIdentifier);
        return true;
      });
      return [...musclesToAdd, ...muscleCollection];
    }
    return muscleCollection;
  }
}
