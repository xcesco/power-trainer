import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { Muscle2ExerciseComponent } from '../list/muscle-2-exercise.component';
import { Muscle2ExerciseDetailComponent } from '../detail/muscle-2-exercise-detail.component';
import { Muscle2ExerciseUpdateComponent } from '../update/muscle-2-exercise-update.component';
import { Muscle2ExerciseRoutingResolveService } from './muscle-2-exercise-routing-resolve.service';

const muscle2ExerciseRoute: Routes = [
  {
    path: '',
    component: Muscle2ExerciseComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: Muscle2ExerciseDetailComponent,
    resolve: {
      muscle2Exercise: Muscle2ExerciseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: Muscle2ExerciseUpdateComponent,
    resolve: {
      muscle2Exercise: Muscle2ExerciseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: Muscle2ExerciseUpdateComponent,
    resolve: {
      muscle2Exercise: Muscle2ExerciseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(muscle2ExerciseRoute)],
  exports: [RouterModule],
})
export class Muscle2ExerciseRoutingModule {}
