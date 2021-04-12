import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IExerciseValue, ExerciseValue } from '../exercise-value.model';
import { ExerciseValueService } from '../service/exercise-value.service';

@Injectable({ providedIn: 'root' })
export class ExerciseValueRoutingResolveService implements Resolve<IExerciseValue> {
  constructor(protected service: ExerciseValueService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IExerciseValue> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((exerciseValue: HttpResponse<ExerciseValue>) => {
          if (exerciseValue.body) {
            return of(exerciseValue.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ExerciseValue());
  }
}
