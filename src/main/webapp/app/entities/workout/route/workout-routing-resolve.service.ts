import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWorkout, Workout } from '../workout.model';
import { WorkoutService } from '../service/workout.service';

@Injectable({ providedIn: 'root' })
export class WorkoutRoutingResolveService implements Resolve<IWorkout> {
  constructor(protected service: WorkoutService, protected router: Router) {}

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
    return of(new Workout());
  }
}
