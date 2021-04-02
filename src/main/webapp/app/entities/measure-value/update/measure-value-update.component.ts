import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IMeasureValue, MeasureValue } from '../measure-value.model';
import { MeasureValueService } from '../service/measure-value.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-measure-value-update',
  templateUrl: './measure-value-update.component.html',
})
export class MeasureValueUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    uuid: [null, [Validators.required]],
    date: [null, [Validators.required]],
    value: [null, [Validators.required]],
    note: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected measureValueService: MeasureValueService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ measureValue }) => {
      if (measureValue.id === undefined) {
        const today = dayjs().startOf('day');
        measureValue.date = today;
      }

      this.updateForm(measureValue);
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

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const measureValue = this.createFromForm();
    if (measureValue.id !== undefined) {
      this.subscribeToSaveResponse(this.measureValueService.update(measureValue));
    } else {
      this.subscribeToSaveResponse(this.measureValueService.create(measureValue));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMeasureValue>>): void {
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

  protected updateForm(measureValue: IMeasureValue): void {
    this.editForm.patchValue({
      id: measureValue.id,
      uuid: measureValue.uuid,
      date: measureValue.date ? measureValue.date.format(DATE_TIME_FORMAT) : null,
      value: measureValue.value,
      note: measureValue.note,
    });
  }

  protected createFromForm(): IMeasureValue {
    return {
      ...new MeasureValue(),
      id: this.editForm.get(['id'])!.value,
      uuid: this.editForm.get(['uuid'])!.value,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      value: this.editForm.get(['value'])!.value,
      note: this.editForm.get(['note'])!.value,
    };
  }
}
