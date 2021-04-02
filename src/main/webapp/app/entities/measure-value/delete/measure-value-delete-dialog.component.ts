import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMeasureValue } from '../measure-value.model';
import { MeasureValueService } from '../service/measure-value.service';

@Component({
  templateUrl: './measure-value-delete-dialog.component.html',
})
export class MeasureValueDeleteDialogComponent {
  measureValue?: IMeasureValue;

  constructor(protected measureValueService: MeasureValueService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.measureValueService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
