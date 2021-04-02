import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IWorkoutSheetExercise, WorkoutSheetExercise } from '../workout-sheet-exercise.model';
import { WorkoutSheetExerciseService } from '../service/workout-sheet-exercise.service';

@Component({
  selector: 'jhi-workout-sheet-exercise-update',
  templateUrl: './workout-sheet-exercise-update.component.html',
})
export class WorkoutSheetExerciseUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    uuid: [null, [Validators.required]],
    order: [],
    repetition: [],
    value: [],
    valueType: [],
  });

  constructor(
    protected workoutSheetExerciseService: WorkoutSheetExerciseService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ workoutSheetExercise }) => {
      this.updateForm(workoutSheetExercise);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const workoutSheetExercise = this.createFromForm();
    if (workoutSheetExercise.id !== undefined) {
      this.subscribeToSaveResponse(this.workoutSheetExerciseService.update(workoutSheetExercise));
    } else {
      this.subscribeToSaveResponse(this.workoutSheetExerciseService.create(workoutSheetExercise));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWorkoutSheetExercise>>): void {
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

  protected updateForm(workoutSheetExercise: IWorkoutSheetExercise): void {
    this.editForm.patchValue({
      id: workoutSheetExercise.id,
      uuid: workoutSheetExercise.uuid,
      order: workoutSheetExercise.order,
      repetition: workoutSheetExercise.repetition,
      value: workoutSheetExercise.value,
      valueType: workoutSheetExercise.valueType,
    });
  }

  protected createFromForm(): IWorkoutSheetExercise {
    return {
      ...new WorkoutSheetExercise(),
      id: this.editForm.get(['id'])!.value,
      uuid: this.editForm.get(['uuid'])!.value,
      order: this.editForm.get(['order'])!.value,
      repetition: this.editForm.get(['repetition'])!.value,
      value: this.editForm.get(['value'])!.value,
      valueType: this.editForm.get(['valueType'])!.value,
    };
  }
}
