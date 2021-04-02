import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IMuscle2Exercise, Muscle2Exercise } from '../muscle-2-exercise.model';
import { Muscle2ExerciseService } from '../service/muscle-2-exercise.service';

@Component({
  selector: 'jhi-muscle-2-exercise-update',
  templateUrl: './muscle-2-exercise-update.component.html',
})
export class Muscle2ExerciseUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
  });

  constructor(
    protected muscle2ExerciseService: Muscle2ExerciseService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ muscle2Exercise }) => {
      this.updateForm(muscle2Exercise);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const muscle2Exercise = this.createFromForm();
    if (muscle2Exercise.id !== undefined) {
      this.subscribeToSaveResponse(this.muscle2ExerciseService.update(muscle2Exercise));
    } else {
      this.subscribeToSaveResponse(this.muscle2ExerciseService.create(muscle2Exercise));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMuscle2Exercise>>): void {
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

  protected updateForm(muscle2Exercise: IMuscle2Exercise): void {
    this.editForm.patchValue({
      id: muscle2Exercise.id,
    });
  }

  protected createFromForm(): IMuscle2Exercise {
    return {
      ...new Muscle2Exercise(),
      id: this.editForm.get(['id'])!.value,
    };
  }
}
