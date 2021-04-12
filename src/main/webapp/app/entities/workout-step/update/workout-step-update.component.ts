import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IWorkoutStep, WorkoutStep } from '../workout-step.model';
import { WorkoutStepService } from '../service/workout-step.service';
import { IWorkout } from 'app/entities/workout/workout.model';
import { WorkoutService } from 'app/entities/workout/service/workout.service';

@Component({
  selector: 'jhi-workout-step-update',
  templateUrl: './workout-step-update.component.html',
})
export class WorkoutStepUpdateComponent implements OnInit {
  isSaving = false;

  workoutsSharedCollection: IWorkout[] = [];

  editForm = this.fb.group({
    id: [],
    uuid: [null, [Validators.required]],
    order: [],
    executionTime: [],
    type: [],
    status: [],
    exerciseUuid: [null, [Validators.required]],
    exerciseName: [null, [Validators.required]],
    exerciseValue: [null, [Validators.required]],
    exerciseValueType: [null, [Validators.required]],
    workout: [],
  });

  constructor(
    protected workoutStepService: WorkoutStepService,
    protected workoutService: WorkoutService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ workoutStep }) => {
      this.updateForm(workoutStep);

      this.loadRelationshipsOptions();
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

  trackWorkoutById(index: number, item: IWorkout): number {
    return item.id!;
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
      executionTime: workoutStep.executionTime,
      type: workoutStep.type,
      status: workoutStep.status,
      exerciseUuid: workoutStep.exerciseUuid,
      exerciseName: workoutStep.exerciseName,
      exerciseValue: workoutStep.exerciseValue,
      exerciseValueType: workoutStep.exerciseValueType,
      workout: workoutStep.workout,
    });

    this.workoutsSharedCollection = this.workoutService.addWorkoutToCollectionIfMissing(this.workoutsSharedCollection, workoutStep.workout);
  }

  protected loadRelationshipsOptions(): void {
    this.workoutService
      .query()
      .pipe(map((res: HttpResponse<IWorkout[]>) => res.body ?? []))
      .pipe(
        map((workouts: IWorkout[]) => this.workoutService.addWorkoutToCollectionIfMissing(workouts, this.editForm.get('workout')!.value))
      )
      .subscribe((workouts: IWorkout[]) => (this.workoutsSharedCollection = workouts));
  }

  protected createFromForm(): IWorkoutStep {
    return {
      ...new WorkoutStep(),
      id: this.editForm.get(['id'])!.value,
      uuid: this.editForm.get(['uuid'])!.value,
      order: this.editForm.get(['order'])!.value,
      executionTime: this.editForm.get(['executionTime'])!.value,
      type: this.editForm.get(['type'])!.value,
      status: this.editForm.get(['status'])!.value,
      exerciseUuid: this.editForm.get(['exerciseUuid'])!.value,
      exerciseName: this.editForm.get(['exerciseName'])!.value,
      exerciseValue: this.editForm.get(['exerciseValue'])!.value,
      exerciseValueType: this.editForm.get(['exerciseValueType'])!.value,
      workout: this.editForm.get(['workout'])!.value,
    };
  }
}
