import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IExerciseValue, ExerciseValue } from '../exercise-value.model';
import { ExerciseValueService } from '../service/exercise-value.service';
import { ICalendar } from 'app/entities/calendar/calendar.model';
import { CalendarService } from 'app/entities/calendar/service/calendar.service';

@Component({
  selector: 'jhi-exercise-value-update',
  templateUrl: './exercise-value-update.component.html',
})
export class ExerciseValueUpdateComponent implements OnInit {
  isSaving = false;

  calendarsSharedCollection: ICalendar[] = [];

  editForm = this.fb.group({
    id: [],
    uuid: [null, [Validators.required]],
    date: [null, [Validators.required]],
    exerciseUuid: [null, [Validators.required]],
    exerciseName: [null, [Validators.required]],
    exerciseValue: [null, [Validators.required]],
    exerciseValueType: [null, [Validators.required]],
    calendar: [],
  });

  constructor(
    protected exerciseValueService: ExerciseValueService,
    protected calendarService: CalendarService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ exerciseValue }) => {
      if (exerciseValue.id === undefined) {
        const today = dayjs().startOf('day');
        exerciseValue.date = today;
      }

      this.updateForm(exerciseValue);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const exerciseValue = this.createFromForm();
    if (exerciseValue.id !== undefined) {
      this.subscribeToSaveResponse(this.exerciseValueService.update(exerciseValue));
    } else {
      this.subscribeToSaveResponse(this.exerciseValueService.create(exerciseValue));
    }
  }

  trackCalendarById(index: number, item: ICalendar): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExerciseValue>>): void {
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

  protected updateForm(exerciseValue: IExerciseValue): void {
    this.editForm.patchValue({
      id: exerciseValue.id,
      uuid: exerciseValue.uuid,
      date: exerciseValue.date ? exerciseValue.date.format(DATE_TIME_FORMAT) : null,
      exerciseUuid: exerciseValue.exerciseUuid,
      exerciseName: exerciseValue.exerciseName,
      exerciseValue: exerciseValue.exerciseValue,
      exerciseValueType: exerciseValue.exerciseValueType,
      calendar: exerciseValue.calendar,
    });

    this.calendarsSharedCollection = this.calendarService.addCalendarToCollectionIfMissing(
      this.calendarsSharedCollection,
      exerciseValue.calendar
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

  protected createFromForm(): IExerciseValue {
    return {
      ...new ExerciseValue(),
      id: this.editForm.get(['id'])!.value,
      uuid: this.editForm.get(['uuid'])!.value,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      exerciseUuid: this.editForm.get(['exerciseUuid'])!.value,
      exerciseName: this.editForm.get(['exerciseName'])!.value,
      exerciseValue: this.editForm.get(['exerciseValue'])!.value,
      exerciseValueType: this.editForm.get(['exerciseValueType'])!.value,
      calendar: this.editForm.get(['calendar'])!.value,
    };
  }
}
