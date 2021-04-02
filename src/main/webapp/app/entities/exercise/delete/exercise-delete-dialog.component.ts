import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IExercise } from '../exercise.model';
import { ExerciseService } from '../service/exercise.service';

@Component({
  templateUrl: './exercise-delete-dialog.component.html',
})
export class ExerciseDeleteDialogComponent {
  exercise?: IExercise;

  constructor(protected exerciseService: ExerciseService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.exerciseService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
