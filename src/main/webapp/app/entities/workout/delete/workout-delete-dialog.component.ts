import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IWorkout } from '../workout.model';
import { WorkoutService } from '../service/workout.service';

@Component({
  templateUrl: './workout-delete-dialog.component.html',
})
export class WorkoutDeleteDialogComponent {
  workout?: IWorkout;

  constructor(protected workoutService: WorkoutService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.workoutService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
