import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IExerciseResource, ExerciseResource } from '../exercise-resource.model';
import { ExerciseResourceService } from '../service/exercise-resource.service';

@Injectable({ providedIn: 'root' })
export class ExerciseResourceRoutingResolveService implements Resolve<IExerciseResource> {
  constructor(protected service: ExerciseResourceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IExerciseResource> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((exerciseResource: HttpResponse<ExerciseResource>) => {
          if (exerciseResource.body) {
            return of(exerciseResource.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ExerciseResource());
  }
}
