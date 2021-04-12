import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IWorkoutSheet } from '../workout-sheet.model';
import { WorkoutSheetService } from '../service/workout-sheet.service';

@Component({
  templateUrl: './workout-sheet-delete-dialog.component.html',
})
export class WorkoutSheetDeleteDialogComponent {
  workoutSheet?: IWorkoutSheet;

  constructor(protected workoutSheetService: WorkoutSheetService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.workoutSheetService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
