import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MuscleComponent } from '../list/muscle.component';
import { MuscleDetailComponent } from '../detail/muscle-detail.component';
import { MuscleUpdateComponent } from '../update/muscle-update.component';
import { MuscleRoutingResolveService } from './muscle-routing-resolve.service';

const muscleRoute: Routes = [
  {
    path: '',
    component: MuscleComponent,
    data: {
      defaultSort: 'name,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MuscleDetailComponent,
    resolve: {
      muscle: MuscleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MuscleUpdateComponent,
    resolve: {
      muscle: MuscleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MuscleUpdateComponent,
    resolve: {
      muscle: MuscleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(muscleRoute)],
  exports: [RouterModule],
})
export class MuscleRoutingModule {}
