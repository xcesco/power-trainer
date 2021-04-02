import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IExerciseValue } from '../exercise-value.model';
import { ExerciseValueService } from '../service/exercise-value.service';

@Component({
  templateUrl: './exercise-value-delete-dialog.component.html',
})
export class ExerciseValueDeleteDialogComponent {
  exerciseValue?: IExerciseValue;

  constructor(protected exerciseValueService: ExerciseValueService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.exerciseValueService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
