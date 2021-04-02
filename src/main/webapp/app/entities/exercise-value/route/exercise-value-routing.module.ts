import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ExerciseValueComponent } from '../list/exercise-value.component';
import { ExerciseValueDetailComponent } from '../detail/exercise-value-detail.component';
import { ExerciseValueUpdateComponent } from '../update/exercise-value-update.component';
import { ExerciseValueRoutingResolveService } from './exercise-value-routing-resolve.service';

const exerciseValueRoute: Routes = [
  {
    path: '',
    component: ExerciseValueComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ExerciseValueDetailComponent,
    resolve: {
      exerciseValue: ExerciseValueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ExerciseValueUpdateComponent,
    resolve: {
      exerciseValue: ExerciseValueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ExerciseValueUpdateComponent,
    resolve: {
      exerciseValue: ExerciseValueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(exerciseValueRoute)],
  exports: [RouterModule],
})
export class ExerciseValueRoutingModule {}
