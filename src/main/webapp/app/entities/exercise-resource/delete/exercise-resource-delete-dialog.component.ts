import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IExerciseResource } from '../exercise-resource.model';
import { ExerciseResourceService } from '../service/exercise-resource.service';

@Component({
  templateUrl: './exercise-resource-delete-dialog.component.html',
})
export class ExerciseResourceDeleteDialogComponent {
  exerciseResource?: IExerciseResource;

  constructor(protected exerciseResourceService: ExerciseResourceService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.exerciseResourceService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
