import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ExerciseToolComponent } from '../list/exercise-tool.component';
import { ExerciseToolDetailComponent } from '../detail/exercise-tool-detail.component';
import { ExerciseToolUpdateComponent } from '../update/exercise-tool-update.component';
import { ExerciseToolRoutingResolveService } from './exercise-tool-routing-resolve.service';

const exerciseToolRoute: Routes = [
  {
    path: '',
    component: ExerciseToolComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ExerciseToolDetailComponent,
    resolve: {
      exerciseTool: ExerciseToolRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ExerciseToolUpdateComponent,
    resolve: {
      exerciseTool: ExerciseToolRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ExerciseToolUpdateComponent,
    resolve: {
      exerciseTool: ExerciseToolRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(exerciseToolRoute)],
  exports: [RouterModule],
})
export class ExerciseToolRoutingModule {}
