import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMuscle2Exercise, Muscle2Exercise } from '../muscle-2-exercise.model';
import { Muscle2ExerciseService } from '../service/muscle-2-exercise.service';

@Injectable({ providedIn: 'root' })
export class Muscle2ExerciseRoutingResolveService implements Resolve<IMuscle2Exercise> {
  constructor(protected service: Muscle2ExerciseService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMuscle2Exercise> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((muscle2Exercise: HttpResponse<Muscle2Exercise>) => {
          if (muscle2Exercise.body) {
            return of(muscle2Exercise.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Muscle2Exercise());
  }
}
