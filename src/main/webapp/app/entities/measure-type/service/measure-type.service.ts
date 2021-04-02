import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMeasureType, getMeasureTypeIdentifier } from '../measure-type.model';

export type EntityResponseType = HttpResponse<IMeasureType>;
export type EntityArrayResponseType = HttpResponse<IMeasureType[]>;

@Injectable({ providedIn: 'root' })
export class MeasureTypeService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/measure-types');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(measureType: IMeasureType): Observable<EntityResponseType> {
    return this.http.post<IMeasureType>(this.resourceUrl, measureType, { observe: 'response' });
  }

  update(measureType: IMeasureType): Observable<EntityResponseType> {
    return this.http.put<IMeasureType>(`${this.resourceUrl}/${getMeasureTypeIdentifier(measureType) as number}`, measureType, {
      observe: 'response',
    });
  }

  partialUpdate(measureType: IMeasureType): Observable<EntityResponseType> {
    return this.http.patch<IMeasureType>(`${this.resourceUrl}/${getMeasureTypeIdentifier(measureType) as number}`, measureType, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMeasureType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMeasureType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMeasureTypeToCollectionIfMissing(
    measureTypeCollection: IMeasureType[],
    ...measureTypesToCheck: (IMeasureType | null | undefined)[]
  ): IMeasureType[] {
    const measureTypes: IMeasureType[] = measureTypesToCheck.filter(isPresent);
    if (measureTypes.length > 0) {
      const measureTypeCollectionIdentifiers = measureTypeCollection.map(measureTypeItem => getMeasureTypeIdentifier(measureTypeItem)!);
      const measureTypesToAdd = measureTypes.filter(measureTypeItem => {
        const measureTypeIdentifier = getMeasureTypeIdentifier(measureTypeItem);
        if (measureTypeIdentifier == null || measureTypeCollectionIdentifiers.includes(measureTypeIdentifier)) {
          return false;
        }
        measureTypeCollectionIdentifiers.push(measureTypeIdentifier);
        return true;
      });
      return [...measureTypesToAdd, ...measureTypeCollection];
    }
    return measureTypeCollection;
  }
}
