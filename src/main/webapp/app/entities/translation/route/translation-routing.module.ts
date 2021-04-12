import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TranslationComponent } from '../list/translation.component';
import { TranslationDetailComponent } from '../detail/translation-detail.component';
import { TranslationUpdateComponent } from '../update/translation-update.component';
import { TranslationRoutingResolveService } from './translation-routing-resolve.service';

const translationRoute: Routes = [
  {
    path: '',
    component: TranslationComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TranslationDetailComponent,
    resolve: {
      translation: TranslationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TranslationUpdateComponent,
    resolve: {
      translation: TranslationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TranslationUpdateComponent,
    resolve: {
      translation: TranslationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(translationRoute)],
  exports: [RouterModule],
})
export class TranslationRoutingModule {}
