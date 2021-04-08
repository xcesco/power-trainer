import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMisuration } from '../misuration.model';
import { MisurationService } from '../service/misuration.service';

@Component({
  templateUrl: './misuration-delete-dialog.component.html',
})
export class MisurationDeleteDialogComponent {
  misuration?: IMisuration;

  constructor(protected misurationService: MisurationService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.misurationService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
