import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { NoteComponent } from './list/note.component';
import { NoteDetailComponent } from './detail/note-detail.component';
import { NoteUpdateComponent } from './update/note-update.component';
import { NoteDeleteDialogComponent } from './delete/note-delete-dialog.component';
import { NoteRoutingModule } from './route/note-routing.module';

@NgModule({
  imports: [SharedModule, NoteRoutingModule],
  declarations: [NoteComponent, NoteDetailComponent, NoteUpdateComponent, NoteDeleteDialogComponent],
  entryComponents: [NoteDeleteDialogComponent],
})
export class NoteModule {}
