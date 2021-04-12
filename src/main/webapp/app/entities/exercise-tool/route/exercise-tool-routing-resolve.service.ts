import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IExerciseTool, ExerciseTool } from '../exercise-tool.model';
import { ExerciseToolService } from '../service/exercise-tool.service';

@Injectable({ providedIn: 'root' })
export class ExerciseToolRoutingResolveService implements Resolve<IExerciseTool> {
  constructor(protected service: ExerciseToolService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IExerciseTool> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((exerciseTool: HttpResponse<ExerciseTool>) => {
          if (exerciseTool.body) {
            return of(exerciseTool.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ExerciseTool());
  }
}
