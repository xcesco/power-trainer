import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { NoteComponent } from '../list/note.component';
import { NoteDetailComponent } from '../detail/note-detail.component';
import { NoteUpdateComponent } from '../update/note-update.component';
import { NoteRoutingResolveService } from './note-routing-resolve.service';

const noteRoute: Routes = [
  {
    path: '',
    component: NoteComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: NoteDetailComponent,
    resolve: {
      note: NoteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: NoteUpdateComponent,
    resolve: {
      note: NoteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: NoteUpdateComponent,
    resolve: {
      note: NoteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(noteRoute)],
  exports: [RouterModule],
})
export class NoteRoutingModule {}
