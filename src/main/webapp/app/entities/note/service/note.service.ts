import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { INote, getNoteIdentifier } from '../note.model';

export type EntityResponseType = HttpResponse<INote>;
export type EntityArrayResponseType = HttpResponse<INote[]>;

@Injectable({ providedIn: 'root' })
export class NoteService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/notes');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(note: INote): Observable<EntityResponseType> {
    return this.http.post<INote>(this.resourceUrl, note, { observe: 'response' });
  }

  update(note: INote): Observable<EntityResponseType> {
    return this.http.put<INote>(`${this.resourceUrl}/${getNoteIdentifier(note) as number}`, note, { observe: 'response' });
  }

  partialUpdate(note: INote): Observable<EntityResponseType> {
    return this.http.patch<INote>(`${this.resourceUrl}/${getNoteIdentifier(note) as number}`, note, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<INote>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<INote[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addNoteToCollectionIfMissing(noteCollection: INote[], ...notesToCheck: (INote | null | undefined)[]): INote[] {
    const notes: INote[] = notesToCheck.filter(isPresent);
    if (notes.length > 0) {
      const noteCollectionIdentifiers = noteCollection.map(noteItem => getNoteIdentifier(noteItem)!);
      const notesToAdd = notes.filter(noteItem => {
        const noteIdentifier = getNoteIdentifier(noteItem);
        if (noteIdentifier == null || noteCollectionIdentifiers.includes(noteIdentifier)) {
          return false;
        }
        noteCollectionIdentifiers.push(noteIdentifier);
        return true;
      });
      return [...notesToAdd, ...noteCollection];
    }
    return noteCollection;
  }
}
