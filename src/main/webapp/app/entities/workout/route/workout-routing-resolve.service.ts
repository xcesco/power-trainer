import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY, merge } from 'rxjs';
import { flatMap, mergeMap } from 'rxjs/operators';

import { IWorkout, Workout } from '../workout.model';
import { WorkoutService } from '../service/workout.service';
import { CalendarService } from 'app/entities/calendar/service/calendar.service';
import { AccountService } from 'app/core/auth/account.service';

@Injectable({ providedIn: 'root' })
export class WorkoutRoutingResolveService implements Resolve<IWorkout> {
  constructor(
    protected service: WorkoutService,
    protected calendarService: CalendarService,
    protected accountService: AccountService,
    protected router: Router
  ) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IWorkout> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((workout: HttpResponse<Workout>) => {
          if (workout.body) {
            return of(workout.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }

    this.accountService.identity().pipe(mergeMap((account: Account) => {}));

    // const example=merge(
    //   this.calendarService.findByOwner(),
    // );
    //
    // return this.calendarService.findByOwner(this.accountService.identity() .pipe(map(identity => {
    //   return
    // })));
    return of(new Workout());
  }
}
