import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICalendar } from '../calendar.model';
import { CalendarService } from '../service/calendar.service';

@Component({
  templateUrl: './calendar-delete-dialog.component.html',
})
export class CalendarDeleteDialogComponent {
  calendar?: ICalendar;

  constructor(protected calendarService: CalendarService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.calendarService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
