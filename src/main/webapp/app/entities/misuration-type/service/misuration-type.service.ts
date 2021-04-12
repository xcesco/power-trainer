import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMisurationType, getMisurationTypeIdentifier } from '../misuration-type.model';

export type EntityResponseType = HttpResponse<IMisurationType>;
export type EntityArrayResponseType = HttpResponse<IMisurationType[]>;

@Injectable({ providedIn: 'root' })
export class MisurationTypeService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/misuration-types');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(misurationType: IMisurationType): Observable<EntityResponseType> {
    return this.http.post<IMisurationType>(this.resourceUrl, misurationType, { observe: 'response' });
  }

  update(misurationType: IMisurationType): Observable<EntityResponseType> {
    return this.http.put<IMisurationType>(`${this.resourceUrl}/${getMisurationTypeIdentifier(misurationType) as number}`, misurationType, {
      observe: 'response',
    });
  }

  partialUpdate(misurationType: IMisurationType): Observable<EntityResponseType> {
    return this.http.patch<IMisurationType>(
      `${this.resourceUrl}/${getMisurationTypeIdentifier(misurationType) as number}`,
      misurationType,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMisurationType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMisurationType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMisurationTypeToCollectionIfMissing(
    misurationTypeCollection: IMisurationType[],
    ...misurationTypesToCheck: (IMisurationType | null | undefined)[]
  ): IMisurationType[] {
    const misurationTypes: IMisurationType[] = misurationTypesToCheck.filter(isPresent);
    if (misurationTypes.length > 0) {
      const misurationTypeCollectionIdentifiers = misurationTypeCollection.map(
        misurationTypeItem => getMisurationTypeIdentifier(misurationTypeItem)!
      );
      const misurationTypesToAdd = misurationTypes.filter(misurationTypeItem => {
        const misurationTypeIdentifier = getMisurationTypeIdentifier(misurationTypeItem);
        if (misurationTypeIdentifier == null || misurationTypeCollectionIdentifiers.includes(misurationTypeIdentifier)) {
          return false;
        }
        misurationTypeCollectionIdentifiers.push(misurationTypeIdentifier);
        return true;
      });
      return [...misurationTypesToAdd, ...misurationTypeCollection];
    }
    return misurationTypeCollection;
  }
}
