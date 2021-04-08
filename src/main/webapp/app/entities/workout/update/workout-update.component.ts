import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IWorkout, Workout } from '../workout.model';
import { WorkoutService } from '../service/workout.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ICalendar } from 'app/entities/calendar/calendar.model';
import { CalendarService } from 'app/entities/calendar/service/calendar.service';

@Component({
  selector: 'jhi-workout-update',
  templateUrl: './workout-update.component.html',
})
export class WorkoutUpdateComponent implements OnInit {
  isSaving = false;

  calendarsSharedCollection: ICalendar[] = [];

  editForm = this.fb.group({
    id: [],
    uuid: [null, [Validators.required]],
    name: [],
    image: [],
    imageContentType: [],
    type: [],
    executionTime: [],
    previewTime: [],
    status: [],
    date: [],
    note: [],
    calendar: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected workoutService: WorkoutService,
    protected calendarService: CalendarService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ workout }) => {
      if (workout.id === undefined) {
        const today = dayjs().startOf('day');
        workout.date = today;
      }

      this.updateForm(workout);

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
    const workout = this.createFromForm();
    if (workout.id !== undefined) {
      this.subscribeToSaveResponse(this.workoutService.update(workout));
    } else {
      this.subscribeToSaveResponse(this.workoutService.create(workout));
    }
  }

  trackCalendarById(index: number, item: ICalendar): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWorkout>>): void {
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

  protected updateForm(workout: IWorkout): void {
    this.editForm.patchValue({
      id: workout.id,
      uuid: workout.uuid,
      name: workout.name,
      image: workout.image,
      imageContentType: workout.imageContentType,
      type: workout.type,
      executionTime: workout.executionTime,
      previewTime: workout.previewTime,
      status: workout.status,
      date: workout.date ? workout.date.format(DATE_TIME_FORMAT) : null,
      note: workout.note,
      calendar: workout.calendar,
    });

    this.calendarsSharedCollection = this.calendarService.addCalendarToCollectionIfMissing(
      this.calendarsSharedCollection,
      workout.calendar
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
  }

  protected createFromForm(): IWorkout {
    return {
      ...new Workout(),
      id: this.editForm.get(['id'])!.value,
      uuid: this.editForm.get(['uuid'])!.value,
      name: this.editForm.get(['name'])!.value,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
      type: this.editForm.get(['type'])!.value,
      executionTime: this.editForm.get(['executionTime'])!.value,
      previewTime: this.editForm.get(['previewTime'])!.value,
      status: this.editForm.get(['status'])!.value,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      note: this.editForm.get(['note'])!.value,
      calendar: this.editForm.get(['calendar'])!.value,
    };
  }
}
