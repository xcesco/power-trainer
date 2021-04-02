import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MeasureValueComponent } from '../list/measure-value.component';
import { MeasureValueDetailComponent } from '../detail/measure-value-detail.component';
import { MeasureValueUpdateComponent } from '../update/measure-value-update.component';
import { MeasureValueRoutingResolveService } from './measure-value-routing-resolve.service';

const measureValueRoute: Routes = [
  {
    path: '',
    component: MeasureValueComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MeasureValueDetailComponent,
    resolve: {
      measureValue: MeasureValueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MeasureValueUpdateComponent,
    resolve: {
      measureValue: MeasureValueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MeasureValueUpdateComponent,
    resolve: {
      measureValue: MeasureValueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(measureValueRoute)],
  exports: [RouterModule],
})
export class MeasureValueRoutingModule {}
