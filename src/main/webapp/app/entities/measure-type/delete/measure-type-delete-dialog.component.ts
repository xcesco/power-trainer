import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMeasureType } from '../measure-type.model';
import { MeasureTypeService } from '../service/measure-type.service';

@Component({
  templateUrl: './measure-type-delete-dialog.component.html',
})
export class MeasureTypeDeleteDialogComponent {
  measureType?: IMeasureType;

  constructor(protected measureTypeService: MeasureTypeService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.measureTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
