import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { INote } from '../note.model';
import { NoteService } from '../service/note.service';

@Component({
  templateUrl: './note-delete-dialog.component.html',
})
export class NoteDeleteDialogComponent {
  note?: INote;

  constructor(protected noteService: NoteService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.noteService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
