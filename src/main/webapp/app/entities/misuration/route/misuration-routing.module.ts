import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MisurationComponent } from '../list/misuration.component';
import { MisurationDetailComponent } from '../detail/misuration-detail.component';
import { MisurationUpdateComponent } from '../update/misuration-update.component';
import { MisurationRoutingResolveService } from './misuration-routing-resolve.service';

const misurationRoute: Routes = [
  {
    path: '',
    component: MisurationComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MisurationDetailComponent,
    resolve: {
      misuration: MisurationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MisurationUpdateComponent,
    resolve: {
      misuration: MisurationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MisurationUpdateComponent,
    resolve: {
      misuration: MisurationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(misurationRoute)],
  exports: [RouterModule],
})
export class MisurationRoutingModule {}
