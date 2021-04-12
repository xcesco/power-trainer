import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWorkoutSheet, getWorkoutSheetIdentifier } from '../workout-sheet.model';

export type EntityResponseType = HttpResponse<IWorkoutSheet>;
export type EntityArrayResponseType = HttpResponse<IWorkoutSheet[]>;

@Injectable({ providedIn: 'root' })
export class WorkoutSheetService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/workout-sheets');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(workoutSheet: IWorkoutSheet): Observable<EntityResponseType> {
    return this.http.post<IWorkoutSheet>(this.resourceUrl, workoutSheet, { observe: 'response' });
  }

  update(workoutSheet: IWorkoutSheet): Observable<EntityResponseType> {
    return this.http.put<IWorkoutSheet>(`${this.resourceUrl}/${getWorkoutSheetIdentifier(workoutSheet) as number}`, workoutSheet, {
      observe: 'response',
    });
  }

  partialUpdate(workoutSheet: IWorkoutSheet): Observable<EntityResponseType> {
    return this.http.patch<IWorkoutSheet>(`${this.resourceUrl}/${getWorkoutSheetIdentifier(workoutSheet) as number}`, workoutSheet, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IWorkoutSheet>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWorkoutSheet[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addWorkoutSheetToCollectionIfMissing(
    workoutSheetCollection: IWorkoutSheet[],
    ...workoutSheetsToCheck: (IWorkoutSheet | null | undefined)[]
  ): IWorkoutSheet[] {
    const workoutSheets: IWorkoutSheet[] = workoutSheetsToCheck.filter(isPresent);
    if (workoutSheets.length > 0) {
      const workoutSheetCollectionIdentifiers = workoutSheetCollection.map(
        workoutSheetItem => getWorkoutSheetIdentifier(workoutSheetItem)!
      );
      const workoutSheetsToAdd = workoutSheets.filter(workoutSheetItem => {
        const workoutSheetIdentifier = getWorkoutSheetIdentifier(workoutSheetItem);
        if (workoutSheetIdentifier == null || workoutSheetCollectionIdentifiers.includes(workoutSheetIdentifier)) {
          return false;
        }
        workoutSheetCollectionIdentifiers.push(workoutSheetIdentifier);
        return true;
      });
      return [...workoutSheetsToAdd, ...workoutSheetCollection];
    }
    return workoutSheetCollection;
  }
}
