import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWorkoutSheet, WorkoutSheet } from '../workout-sheet.model';
import { WorkoutSheetService } from '../service/workout-sheet.service';

@Injectable({ providedIn: 'root' })
export class WorkoutSheetRoutingResolveService implements Resolve<IWorkoutSheet> {
  constructor(protected service: WorkoutSheetService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IWorkoutSheet> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((workoutSheet: HttpResponse<WorkoutSheet>) => {
          if (workoutSheet.body) {
            return of(workoutSheet.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new WorkoutSheet());
  }
}
