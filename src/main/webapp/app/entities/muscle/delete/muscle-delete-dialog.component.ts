import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMuscle } from '../muscle.model';
import { MuscleService } from '../service/muscle.service';

@Component({
  templateUrl: './muscle-delete-dialog.component.html',
})
export class MuscleDeleteDialogComponent {
  muscle?: IMuscle;

  constructor(protected muscleService: MuscleService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.muscleService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
