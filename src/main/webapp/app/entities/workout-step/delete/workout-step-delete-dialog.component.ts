import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IWorkoutStep } from '../workout-step.model';
import { WorkoutStepService } from '../service/workout-step.service';

@Component({
  templateUrl: './workout-step-delete-dialog.component.html',
})
export class WorkoutStepDeleteDialogComponent {
  workoutStep?: IWorkoutStep;

  constructor(protected workoutStepService: WorkoutStepService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.workoutStepService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
