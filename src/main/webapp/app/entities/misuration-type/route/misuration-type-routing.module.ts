import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MisurationTypeComponent } from '../list/misuration-type.component';
import { MisurationTypeDetailComponent } from '../detail/misuration-type-detail.component';
import { MisurationTypeUpdateComponent } from '../update/misuration-type-update.component';
import { MisurationTypeRoutingResolveService } from './misuration-type-routing-resolve.service';

const misurationTypeRoute: Routes = [
  {
    path: '',
    component: MisurationTypeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MisurationTypeDetailComponent,
    resolve: {
      misurationType: MisurationTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MisurationTypeUpdateComponent,
    resolve: {
      misurationType: MisurationTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MisurationTypeUpdateComponent,
    resolve: {
      misurationType: MisurationTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(misurationTypeRoute)],
  exports: [RouterModule],
})
export class MisurationTypeRoutingModule {}
