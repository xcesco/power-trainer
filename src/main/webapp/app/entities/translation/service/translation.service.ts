import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITranslation, getTranslationIdentifier } from '../translation.model';

export type EntityResponseType = HttpResponse<ITranslation>;
export type EntityArrayResponseType = HttpResponse<ITranslation[]>;

@Injectable({ providedIn: 'root' })
export class TranslationService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/translations');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(translation: ITranslation): Observable<EntityResponseType> {
    return this.http.post<ITranslation>(this.resourceUrl, translation, { observe: 'response' });
  }

  update(translation: ITranslation): Observable<EntityResponseType> {
    return this.http.put<ITranslation>(`${this.resourceUrl}/${getTranslationIdentifier(translation) as number}`, translation, {
      observe: 'response',
    });
  }

  partialUpdate(translation: ITranslation): Observable<EntityResponseType> {
    return this.http.patch<ITranslation>(`${this.resourceUrl}/${getTranslationIdentifier(translation) as number}`, translation, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITranslation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITranslation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTranslationToCollectionIfMissing(
    translationCollection: ITranslation[],
    ...translationsToCheck: (ITranslation | null | undefined)[]
  ): ITranslation[] {
    const translations: ITranslation[] = translationsToCheck.filter(isPresent);
    if (translations.length > 0) {
      const translationCollectionIdentifiers = translationCollection.map(translationItem => getTranslationIdentifier(translationItem)!);
      const translationsToAdd = translations.filter(translationItem => {
        const translationIdentifier = getTranslationIdentifier(translationItem);
        if (translationIdentifier == null || translationCollectionIdentifiers.includes(translationIdentifier)) {
          return false;
        }
        translationCollectionIdentifiers.push(translationIdentifier);
        return true;
      });
      return [...translationsToAdd, ...translationCollection];
    }
    return translationCollection;
  }
}
