import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IExerciseValue, ExerciseValue } from '../exercise-value.model';
import { ExerciseValueService } from '../service/exercise-value.service';

@Component({
  selector: 'jhi-exercise-value-update',
  templateUrl: './exercise-value-update.component.html',
})
export class ExerciseValueUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    uuid: [null, [Validators.required]],
    value: [null, [Validators.required]],
    date: [null, [Validators.required]],
  });

  constructor(protected exerciseValueService: ExerciseValueService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ exerciseValue }) => {
      this.updateForm(exerciseValue);
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
      value: exerciseValue.value,
      date: exerciseValue.date,
    });
  }

  protected createFromForm(): IExerciseValue {
    return {
      ...new ExerciseValue(),
      id: this.editForm.get(['id'])!.value,
      uuid: this.editForm.get(['uuid'])!.value,
      value: this.editForm.get(['value'])!.value,
      date: this.editForm.get(['date'])!.value,
    };
  }
}
