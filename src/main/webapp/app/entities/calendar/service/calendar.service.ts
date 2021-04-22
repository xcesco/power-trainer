import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICalendar, getCalendarIdentifier } from '../calendar.model';

export type EntityResponseType = HttpResponse<ICalendar>;
export type EntityArrayResponseType = HttpResponse<ICalendar[]>;

@Injectable({ providedIn: 'root' })
export class CalendarService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/calendars');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(calendar: ICalendar): Observable<EntityResponseType> {
    return this.http.post<ICalendar>(this.resourceUrl, calendar, { observe: 'response' });
  }

  update(calendar: ICalendar): Observable<EntityResponseType> {
    return this.http.put<ICalendar>(`${this.resourceUrl}/${getCalendarIdentifier(calendar) as number}`, calendar, { observe: 'response' });
  }

  partialUpdate(calendar: ICalendar): Observable<EntityResponseType> {
    return this.http.patch<ICalendar>(`${this.resourceUrl}/${getCalendarIdentifier(calendar) as number}`, calendar, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICalendar>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  findByOwner(owner: string): Observable<EntityArrayResponseType> {
    const options = createRequestOption({ 'owner.equals': owner });
    return this.http.get<ICalendar[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICalendar[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCalendarToCollectionIfMissing(calendarCollection: ICalendar[], ...calendarsToCheck: (ICalendar | null | undefined)[]): ICalendar[] {
    const calendars: ICalendar[] = calendarsToCheck.filter(isPresent);
    if (calendars.length > 0) {
      const calendarCollectionIdentifiers = calendarCollection.map(calendarItem => getCalendarIdentifier(calendarItem)!);
      const calendarsToAdd = calendars.filter(calendarItem => {
        const calendarIdentifier = getCalendarIdentifier(calendarItem);
        if (calendarIdentifier == null || calendarCollectionIdentifiers.includes(calendarIdentifier)) {
          return false;
        }
        calendarCollectionIdentifiers.push(calendarIdentifier);
        return true;
      });
      return [...calendarsToAdd, ...calendarCollection];
    }
    return calendarCollection;
  }
}
