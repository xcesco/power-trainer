import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IWorkoutSheetExercise } from '../workout-sheet-exercise.model';
import { WorkoutSheetExerciseService } from '../service/workout-sheet-exercise.service';

@Component({
  templateUrl: './workout-sheet-exercise-delete-dialog.component.html',
})
export class WorkoutSheetExerciseDeleteDialogComponent {
  workoutSheetExercise?: IWorkoutSheetExercise;

  constructor(protected workoutSheetExerciseService: WorkoutSheetExerciseService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.workoutSheetExerciseService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
