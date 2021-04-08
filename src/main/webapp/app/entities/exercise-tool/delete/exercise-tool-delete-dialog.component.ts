import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IExerciseTool } from '../exercise-tool.model';
import { ExerciseToolService } from '../service/exercise-tool.service';

@Component({
  templateUrl: './exercise-tool-delete-dialog.component.html',
})
export class ExerciseToolDeleteDialogComponent {
  exerciseTool?: IExerciseTool;

  constructor(protected exerciseToolService: ExerciseToolService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.exerciseToolService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
