import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWorkoutSheetExercise, WorkoutSheetExercise } from '../workout-sheet-exercise.model';
import { WorkoutSheetExerciseService } from '../service/workout-sheet-exercise.service';

@Injectable({ providedIn: 'root' })
export class WorkoutSheetExerciseRoutingResolveService implements Resolve<IWorkoutSheetExercise> {
  constructor(protected service: WorkoutSheetExerciseService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IWorkoutSheetExercise> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((workoutSheetExercise: HttpResponse<WorkoutSheetExercise>) => {
          if (workoutSheetExercise.body) {
            return of(workoutSheetExercise.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new WorkoutSheetExercise());
  }
}
