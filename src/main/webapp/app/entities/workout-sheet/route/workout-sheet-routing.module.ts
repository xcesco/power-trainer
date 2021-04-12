import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WorkoutSheetComponent } from '../list/workout-sheet.component';
import { WorkoutSheetDetailComponent } from '../detail/workout-sheet-detail.component';
import { WorkoutSheetUpdateComponent } from '../update/workout-sheet-update.component';
import { WorkoutSheetRoutingResolveService } from './workout-sheet-routing-resolve.service';

const workoutSheetRoute: Routes = [
  {
    path: '',
    component: WorkoutSheetComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WorkoutSheetDetailComponent,
    resolve: {
      workoutSheet: WorkoutSheetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WorkoutSheetUpdateComponent,
    resolve: {
      workoutSheet: WorkoutSheetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WorkoutSheetUpdateComponent,
    resolve: {
      workoutSheet: WorkoutSheetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(workoutSheetRoute)],
  exports: [RouterModule],
})
export class WorkoutSheetRoutingModule {}
