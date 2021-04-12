import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMisuration, getMisurationIdentifier } from '../misuration.model';

export type EntityResponseType = HttpResponse<IMisuration>;
export type EntityArrayResponseType = HttpResponse<IMisuration[]>;

@Injectable({ providedIn: 'root' })
export class MisurationService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/misurations');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(misuration: IMisuration): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(misuration);
    return this.http
      .post<IMisuration>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(misuration: IMisuration): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(misuration);
    return this.http
      .put<IMisuration>(`${this.resourceUrl}/${getMisurationIdentifier(misuration) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(misuration: IMisuration): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(misuration);
    return this.http
      .patch<IMisuration>(`${this.resourceUrl}/${getMisurationIdentifier(misuration) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IMisuration>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMisuration[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMisurationToCollectionIfMissing(
    misurationCollection: IMisuration[],
    ...misurationsToCheck: (IMisuration | null | undefined)[]
  ): IMisuration[] {
    const misurations: IMisuration[] = misurationsToCheck.filter(isPresent);
    if (misurations.length > 0) {
      const misurationCollectionIdentifiers = misurationCollection.map(misurationItem => getMisurationIdentifier(misurationItem)!);
      const misurationsToAdd = misurations.filter(misurationItem => {
        const misurationIdentifier = getMisurationIdentifier(misurationItem);
        if (misurationIdentifier == null || misurationCollectionIdentifiers.includes(misurationIdentifier)) {
          return false;
        }
        misurationCollectionIdentifiers.push(misurationIdentifier);
        return true;
      });
      return [...misurationsToAdd, ...misurationCollection];
    }
    return misurationCollection;
  }

  protected convertDateFromClient(misuration: IMisuration): IMisuration {
    return Object.assign({}, misuration, {
      date: misuration.date?.isValid() ? misuration.date.toJSON() : undefined,
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
      res.body.forEach((misuration: IMisuration) => {
        misuration.date = misuration.date ? dayjs(misuration.date) : undefined;
      });
    }
    return res;
  }
}
