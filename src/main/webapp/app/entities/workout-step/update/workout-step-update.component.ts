import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IWorkoutStep, WorkoutStep } from '../workout-step.model';
import { WorkoutStepService } from '../service/workout-step.service';

@Component({
  selector: 'jhi-workout-step-update',
  templateUrl: './workout-step-update.component.html',
})
export class WorkoutStepUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    uuid: [null, [Validators.required]],
    order: [],
    value: [],
    valueType: [],
    executionTime: [],
    type: [],
    status: [],
  });

  constructor(protected workoutStepService: WorkoutStepService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ workoutStep }) => {
      this.updateForm(workoutStep);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const workoutStep = this.createFromForm();
    if (workoutStep.id !== undefined) {
      this.subscribeToSaveResponse(this.workoutStepService.update(workoutStep));
    } else {
      this.subscribeToSaveResponse(this.workoutStepService.create(workoutStep));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWorkoutStep>>): void {
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

  protected updateForm(workoutStep: IWorkoutStep): void {
    this.editForm.patchValue({
      id: workoutStep.id,
      uuid: workoutStep.uuid,
      order: workoutStep.order,
      value: workoutStep.value,
      valueType: workoutStep.valueType,
      executionTime: workoutStep.executionTime,
      type: workoutStep.type,
      status: workoutStep.status,
    });
  }

  protected createFromForm(): IWorkoutStep {
    return {
      ...new WorkoutStep(),
      id: this.editForm.get(['id'])!.value,
      uuid: this.editForm.get(['uuid'])!.value,
      order: this.editForm.get(['order'])!.value,
      value: this.editForm.get(['value'])!.value,
      valueType: this.editForm.get(['valueType'])!.value,
      executionTime: this.editForm.get(['executionTime'])!.value,
      type: this.editForm.get(['type'])!.value,
      status: this.editForm.get(['status'])!.value,
    };
  }
}
