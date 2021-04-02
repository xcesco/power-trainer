import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMuscle2Exercise } from '../muscle-2-exercise.model';
import { Muscle2ExerciseService } from '../service/muscle-2-exercise.service';

@Component({
  templateUrl: './muscle-2-exercise-delete-dialog.component.html',
})
export class Muscle2ExerciseDeleteDialogComponent {
  muscle2Exercise?: IMuscle2Exercise;

  constructor(protected muscle2ExerciseService: Muscle2ExerciseService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.muscle2ExerciseService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
