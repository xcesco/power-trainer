import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IMisuration, Misuration } from '../misuration.model';
import { MisurationService } from '../service/misuration.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ICalendar } from 'app/entities/calendar/calendar.model';
import { CalendarService } from 'app/entities/calendar/service/calendar.service';
import { IMisurationType } from 'app/entities/misuration-type/misuration-type.model';
import { MisurationTypeService } from 'app/entities/misuration-type/service/misuration-type.service';

@Component({
  selector: 'jhi-misuration-update',
  templateUrl: './misuration-update.component.html',
})
export class MisurationUpdateComponent implements OnInit {
  isSaving = false;

  calendarsSharedCollection: ICalendar[] = [];
  misurationTypesSharedCollection: IMisurationType[] = [];

  editForm = this.fb.group({
    id: [],
    uuid: [null, [Validators.required]],
    date: [null, [Validators.required]],
    value: [null, [Validators.required]],
    image: [],
    imageContentType: [],
    note: [],
    calendar: [],
    misurationType: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected misurationService: MisurationService,
    protected calendarService: CalendarService,
    protected misurationTypeService: MisurationTypeService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ misuration }) => {
      if (misuration.id === undefined) {
        const today = dayjs().startOf('day');
        misuration.date = today;
      }

      this.updateForm(misuration);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('powerTrainerApp.error', { ...err, key: 'error.file.' + err.key })
        ),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const misuration = this.createFromForm();
    if (misuration.id !== undefined) {
      this.subscribeToSaveResponse(this.misurationService.update(misuration));
    } else {
      this.subscribeToSaveResponse(this.misurationService.create(misuration));
    }
  }

  trackCalendarById(index: number, item: ICalendar): number {
    return item.id!;
  }

  trackMisurationTypeById(index: number, item: IMisurationType): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMisuration>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(misuration: IMisuration): void {
    this.editForm.patchValue({
      id: misuration.id,
      uuid: misuration.uuid,
      date: misuration.date ? misuration.date.format(DATE_TIME_FORMAT) : null,
      value: misuration.value,
      image: misuration.image,
      imageContentType: misuration.imageContentType,
      note: misuration.note,
      calendar: misuration.calendar,
      misurationType: misuration.misurationType,
    });

    this.calendarsSharedCollection = this.calendarService.addCalendarToCollectionIfMissing(
      this.calendarsSharedCollection,
      misuration.calendar
    );
    this.misurationTypesSharedCollection = this.misurationTypeService.addMisurationTypeToCollectionIfMissing(
      this.misurationTypesSharedCollection,
      misuration.misurationType
    );
  }

  protected loadRelationshipsOptions(): void {
    this.calendarService
      .query()
      .pipe(map((res: HttpResponse<ICalendar[]>) => res.body ?? []))
      .pipe(
        map((calendars: ICalendar[]) =>
          this.calendarService.addCalendarToCollectionIfMissing(calendars, this.editForm.get('calendar')!.value)
        )
      )
      .subscribe((calendars: ICalendar[]) => (this.calendarsSharedCollection = calendars));

    this.misurationTypeService
      .query()
      .pipe(map((res: HttpResponse<IMisurationType[]>) => res.body ?? []))
      .pipe(
        map((misurationTypes: IMisurationType[]) =>
          this.misurationTypeService.addMisurationTypeToCollectionIfMissing(misurationTypes, this.editForm.get('misurationType')!.value)
        )
      )
      .subscribe((misurationTypes: IMisurationType[]) => (this.misurationTypesSharedCollection = misurationTypes));
  }

  protected createFromForm(): IMisuration {
    return {
      ...new Misuration(),
      id: this.editForm.get(['id'])!.value,
      uuid: this.editForm.get(['uuid'])!.value,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      value: this.editForm.get(['value'])!.value,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
      note: this.editForm.get(['note'])!.value,
      calendar: this.editForm.get(['calendar'])!.value,
      misurationType: this.editForm.get(['misurationType'])!.value,
    };
  }
}
