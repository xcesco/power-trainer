import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WorkoutStepComponent } from '../list/workout-step.component';
import { WorkoutStepDetailComponent } from '../detail/workout-step-detail.component';
import { WorkoutStepUpdateComponent } from '../update/workout-step-update.component';
import { WorkoutStepRoutingResolveService } from './workout-step-routing-resolve.service';

const workoutStepRoute: Routes = [
  {
    path: '',
    component: WorkoutStepComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WorkoutStepDetailComponent,
    resolve: {
      workoutStep: WorkoutStepRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WorkoutStepUpdateComponent,
    resolve: {
      workoutStep: WorkoutStepRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WorkoutStepUpdateComponent,
    resolve: {
      workoutStep: WorkoutStepRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(workoutStepRoute)],
  exports: [RouterModule],
})
export class WorkoutStepRoutingModule {}
