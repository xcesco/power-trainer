import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ExerciseResourceComponent } from '../list/exercise-resource.component';
import { ExerciseResourceDetailComponent } from '../detail/exercise-resource-detail.component';
import { ExerciseResourceUpdateComponent } from '../update/exercise-resource-update.component';
import { ExerciseResourceRoutingResolveService } from './exercise-resource-routing-resolve.service';

const exerciseResourceRoute: Routes = [
  {
    path: '',
    component: ExerciseResourceComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ExerciseResourceDetailComponent,
    resolve: {
      exerciseResource: ExerciseResourceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ExerciseResourceUpdateComponent,
    resolve: {
      exerciseResource: ExerciseResourceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ExerciseResourceUpdateComponent,
    resolve: {
      exerciseResource: ExerciseResourceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(exerciseResourceRoute)],
  exports: [RouterModule],
})
export class ExerciseResourceRoutingModule {}
