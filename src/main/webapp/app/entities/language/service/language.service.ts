import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILanguage, getLanguageIdentifier } from '../language.model';

export type EntityResponseType = HttpResponse<ILanguage>;
export type EntityArrayResponseType = HttpResponse<ILanguage[]>;

@Injectable({ providedIn: 'root' })
export class LanguageService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/languages');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(language: ILanguage): Observable<EntityResponseType> {
    return this.http.post<ILanguage>(this.resourceUrl, language, { observe: 'response' });
  }

  update(language: ILanguage): Observable<EntityResponseType> {
    return this.http.put<ILanguage>(`${this.resourceUrl}/${getLanguageIdentifier(language) as number}`, language, { observe: 'response' });
  }

  partialUpdate(language: ILanguage): Observable<EntityResponseType> {
    return this.http.patch<ILanguage>(`${this.resourceUrl}/${getLanguageIdentifier(language) as number}`, language, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILanguage>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILanguage[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLanguageToCollectionIfMissing(languageCollection: ILanguage[], ...languagesToCheck: (ILanguage | null | undefined)[]): ILanguage[] {
    const languages: ILanguage[] = languagesToCheck.filter(isPresent);
    if (languages.length > 0) {
      const languageCollectionIdentifiers = languageCollection.map(languageItem => getLanguageIdentifier(languageItem)!);
      const languagesToAdd = languages.filter(languageItem => {
        const languageIdentifier = getLanguageIdentifier(languageItem);
        if (languageIdentifier == null || languageCollectionIdentifiers.includes(languageIdentifier)) {
          return false;
        }
        languageCollectionIdentifiers.push(languageIdentifier);
        return true;
      });
      return [...languagesToAdd, ...languageCollection];
    }
    return languageCollection;
  }
}
