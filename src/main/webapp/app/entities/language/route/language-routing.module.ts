import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LanguageComponent } from '../list/language.component';
import { LanguageDetailComponent } from '../detail/language-detail.component';
import { LanguageUpdateComponent } from '../update/language-update.component';
import { LanguageRoutingResolveService } from './language-routing-resolve.service';

const languageRoute: Routes = [
  {
    path: '',
    component: LanguageComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LanguageDetailComponent,
    resolve: {
      language: LanguageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LanguageUpdateComponent,
    resolve: {
      language: LanguageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LanguageUpdateComponent,
    resolve: {
      language: LanguageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(languageRoute)],
  exports: [RouterModule],
})
export class LanguageRoutingModule {}
