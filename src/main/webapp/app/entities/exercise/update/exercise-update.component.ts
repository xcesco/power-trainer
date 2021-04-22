import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';
import { v4 as uuid } from 'uuid';

import { IExercise, Exercise } from '../exercise.model';
import { ExerciseService } from '../service/exercise.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IMuscle } from 'app/entities/muscle/muscle.model';
import { MuscleService } from 'app/entities/muscle/service/muscle.service';
import { IExerciseTool } from 'app/entities/exercise-tool/exercise-tool.model';
import { ExerciseToolService } from 'app/entities/exercise-tool/service/exercise-tool.service';

@Component({
  selector: 'jhi-exercise-update',
  templateUrl: './exercise-update.component.html',
})
export class ExerciseUpdateComponent implements OnInit {
  isSaving = false;

  musclesSharedCollection: IMuscle[] = [];
  exerciseToolsSharedCollection: IExerciseTool[] = [];

  editForm = this.fb.group({
    id: [],
    uuid: [null, [Validators.required]],
    image: [],
    imageContentType: [],
    name: [null, [Validators.required]],
    description: [],
    valueType: [],
    owner: [null, [Validators.required]],
    muscles: [],
    exerciseTools: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected exerciseService: ExerciseService,
    protected muscleService: MuscleService,
    protected exerciseToolService: ExerciseToolService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ exercise }) => {
      this.updateForm(exercise);

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
    const exercise = this.createFromForm();
    if (exercise.id !== undefined) {
      this.subscribeToSaveResponse(this.exerciseService.update(exercise));
    } else {
      this.subscribeToSaveResponse(this.exerciseService.create(exercise));
    }
  }

  trackMuscleById(index: number, item: IMuscle): number {
    return item.id!;
  }

  trackExerciseToolById(index: number, item: IExerciseTool): number {
    return item.id!;
  }

  getSelectedMuscle(option: IMuscle, selectedVals?: IMuscle[]): IMuscle {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  getSelectedExerciseTool(option: IExerciseTool, selectedVals?: IExerciseTool[]): IExerciseTool {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExercise>>): void {
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

  protected updateForm(exercise: IExercise): void {
    this.editForm.patchValue({
      id: exercise.id,
      uuid: exercise.uuid,
      image: exercise.image,
      imageContentType: exercise.imageContentType,
      name: exercise.name,
      description: exercise.description,
      valueType: exercise.valueType,
      owner: exercise.owner,
      muscles: exercise.muscles,
      exerciseTools: exercise.exerciseTools,
    });

    this.musclesSharedCollection = this.muscleService.addMuscleToCollectionIfMissing(
      this.musclesSharedCollection,
      ...(exercise.muscles ?? [])
    );
    this.exerciseToolsSharedCollection = this.exerciseToolService.addExerciseToolToCollectionIfMissing(
      this.exerciseToolsSharedCollection,
      ...(exercise.exerciseTools ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.muscleService
      .query()
      .pipe(map((res: HttpResponse<IMuscle[]>) => res.body ?? []))
      .pipe(
        map((muscles: IMuscle[]) =>
          this.muscleService.addMuscleToCollectionIfMissing(muscles, ...(this.editForm.get('muscles')!.value ?? []))
        )
      )
      .subscribe((muscles: IMuscle[]) => (this.musclesSharedCollection = muscles));

    this.exerciseToolService
      .query()
      .pipe(map((res: HttpResponse<IExerciseTool[]>) => res.body ?? []))
      .pipe(
        map((exerciseTools: IExerciseTool[]) =>
          this.exerciseToolService.addExerciseToolToCollectionIfMissing(exerciseTools, ...(this.editForm.get('exerciseTools')!.value ?? []))
        )
      )
      .subscribe((exerciseTools: IExerciseTool[]) => (this.exerciseToolsSharedCollection = exerciseTools));
  }

  protected createFromForm(): IExercise {
    return {
      ...new Exercise(),
      id: this.editForm.get(['id'])!.value,
      uuid: this.editForm.get(['uuid'])!.value,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      valueType: this.editForm.get(['valueType'])!.value,
      owner: this.editForm.get(['owner'])!.value,
      muscles: this.editForm.get(['muscles'])!.value,
      exerciseTools: this.editForm.get(['exerciseTools'])!.value,
    };
  }
}
