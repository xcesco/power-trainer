import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WorkoutSheetExerciseComponent } from '../list/workout-sheet-exercise.component';
import { WorkoutSheetExerciseDetailComponent } from '../detail/workout-sheet-exercise-detail.component';
import { WorkoutSheetExerciseUpdateComponent } from '../update/workout-sheet-exercise-update.component';
import { WorkoutSheetExerciseRoutingResolveService } from './workout-sheet-exercise-routing-resolve.service';

const workoutSheetExerciseRoute: Routes = [
  {
    path: '',
    component: WorkoutSheetExerciseComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WorkoutSheetExerciseDetailComponent,
    resolve: {
      workoutSheetExercise: WorkoutSheetExerciseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WorkoutSheetExerciseUpdateComponent,
    resolve: {
      workoutSheetExercise: WorkoutSheetExerciseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WorkoutSheetExerciseUpdateComponent,
    resolve: {
      workoutSheetExercise: WorkoutSheetExerciseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(workoutSheetExerciseRoute)],
  exports: [RouterModule],
})
export class WorkoutSheetExerciseRoutingModule {}
