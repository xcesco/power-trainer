import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDevice } from '../device.model';
import { DeviceService } from '../service/device.service';

@Component({
  templateUrl: './device-delete-dialog.component.html',
})
export class DeviceDeleteDialogComponent {
  device?: IDevice;

  constructor(protected deviceService: DeviceService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.deviceService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
