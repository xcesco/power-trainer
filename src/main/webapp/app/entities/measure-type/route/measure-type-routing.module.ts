import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MeasureTypeComponent } from '../list/measure-type.component';
import { MeasureTypeDetailComponent } from '../detail/measure-type-detail.component';
import { MeasureTypeUpdateComponent } from '../update/measure-type-update.component';
import { MeasureTypeRoutingResolveService } from './measure-type-routing-resolve.service';

const measureTypeRoute: Routes = [
  {
    path: '',
    component: MeasureTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MeasureTypeDetailComponent,
    resolve: {
      measureType: MeasureTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MeasureTypeUpdateComponent,
    resolve: {
      measureType: MeasureTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MeasureTypeUpdateComponent,
    resolve: {
      measureType: MeasureTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(measureTypeRoute)],
  exports: [RouterModule],
})
export class MeasureTypeRoutingModule {}
