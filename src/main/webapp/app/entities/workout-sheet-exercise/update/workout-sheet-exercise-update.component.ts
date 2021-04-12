import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IWorkoutSheetExercise, WorkoutSheetExercise } from '../workout-sheet-exercise.model';
import { WorkoutSheetExerciseService } from '../service/workout-sheet-exercise.service';
import { IWorkoutSheet } from 'app/entities/workout-sheet/workout-sheet.model';
import { WorkoutSheetService } from 'app/entities/workout-sheet/service/workout-sheet.service';

@Component({
  selector: 'jhi-workout-sheet-exercise-update',
  templateUrl: './workout-sheet-exercise-update.component.html',
})
export class WorkoutSheetExerciseUpdateComponent implements OnInit {
  isSaving = false;

  workoutSheetsSharedCollection: IWorkoutSheet[] = [];

  editForm = this.fb.group({
    id: [],
    uuid: [null, [Validators.required]],
    order: [],
    repetitions: [],
    exerciseUuid: [null, [Validators.required]],
    exerciseName: [null, [Validators.required]],
    exerciseValue: [null, [Validators.required]],
    exerciseValueType: [null, [Validators.required]],
    workoutSheet: [],
  });

  constructor(
    protected workoutSheetExerciseService: WorkoutSheetExerciseService,
    protected workoutSheetService: WorkoutSheetService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ workoutSheetExercise }) => {
      this.updateForm(workoutSheetExercise);

      this.loadRelationshipsOptions();
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

  trackWorkoutSheetById(index: number, item: IWorkoutSheet): number {
    return item.id!;
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
      repetitions: workoutSheetExercise.repetitions,
      exerciseUuid: workoutSheetExercise.exerciseUuid,
      exerciseName: workoutSheetExercise.exerciseName,
      exerciseValue: workoutSheetExercise.exerciseValue,
      exerciseValueType: workoutSheetExercise.exerciseValueType,
      workoutSheet: workoutSheetExercise.workoutSheet,
    });

    this.workoutSheetsSharedCollection = this.workoutSheetService.addWorkoutSheetToCollectionIfMissing(
      this.workoutSheetsSharedCollection,
      workoutSheetExercise.workoutSheet
    );
  }

  protected loadRelationshipsOptions(): void {
    this.workoutSheetService
      .query()
      .pipe(map((res: HttpResponse<IWorkoutSheet[]>) => res.body ?? []))
      .pipe(
        map((workoutSheets: IWorkoutSheet[]) =>
          this.workoutSheetService.addWorkoutSheetToCollectionIfMissing(workoutSheets, this.editForm.get('workoutSheet')!.value)
        )
      )
      .subscribe((workoutSheets: IWorkoutSheet[]) => (this.workoutSheetsSharedCollection = workoutSheets));
  }

  protected createFromForm(): IWorkoutSheetExercise {
    return {
      ...new WorkoutSheetExercise(),
      id: this.editForm.get(['id'])!.value,
      uuid: this.editForm.get(['uuid'])!.value,
      order: this.editForm.get(['order'])!.value,
      repetitions: this.editForm.get(['repetitions'])!.value,
      exerciseUuid: this.editForm.get(['exerciseUuid'])!.value,
      exerciseName: this.editForm.get(['exerciseName'])!.value,
      exerciseValue: this.editForm.get(['exerciseValue'])!.value,
      exerciseValueType: this.editForm.get(['exerciseValueType'])!.value,
      workoutSheet: this.editForm.get(['workoutSheet'])!.value,
    };
  }
}
