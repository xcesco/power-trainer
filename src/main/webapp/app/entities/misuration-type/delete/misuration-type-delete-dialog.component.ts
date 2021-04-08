import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMisurationType } from '../misuration-type.model';
import { MisurationTypeService } from '../service/misuration-type.service';

@Component({
  templateUrl: './misuration-type-delete-dialog.component.html',
})
export class MisurationTypeDeleteDialogComponent {
  misurationType?: IMisurationType;

  constructor(protected misurationTypeService: MisurationTypeService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.misurationTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
