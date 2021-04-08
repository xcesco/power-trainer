import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IExerciseTool, ExerciseTool } from '../exercise-tool.model';
import { ExerciseToolService } from '../service/exercise-tool.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IExercise } from 'app/entities/exercise/exercise.model';
import { ExerciseService } from 'app/entities/exercise/service/exercise.service';

@Component({
  selector: 'jhi-exercise-tool-update',
  templateUrl: './exercise-tool-update.component.html',
})
export class ExerciseToolUpdateComponent implements OnInit {
  isSaving = false;

  exercisesSharedCollection: IExercise[] = [];

  editForm = this.fb.group({
    id: [],
    uuid: [null, [Validators.required]],
    image: [],
    imageContentType: [],
    name: [null, [Validators.required]],
    description: [],
    exercise: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected exerciseToolService: ExerciseToolService,
    protected exerciseService: ExerciseService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ exerciseTool }) => {
      this.updateForm(exerciseTool);

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
    const exerciseTool = this.createFromForm();
    if (exerciseTool.id !== undefined) {
      this.subscribeToSaveResponse(this.exerciseToolService.update(exerciseTool));
    } else {
      this.subscribeToSaveResponse(this.exerciseToolService.create(exerciseTool));
    }
  }

  trackExerciseById(index: number, item: IExercise): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExerciseTool>>): void {
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

  protected updateForm(exerciseTool: IExerciseTool): void {
    this.editForm.patchValue({
      id: exerciseTool.id,
      uuid: exerciseTool.uuid,
      image: exerciseTool.image,
      imageContentType: exerciseTool.imageContentType,
      name: exerciseTool.name,
      description: exerciseTool.description,
      exercise: exerciseTool.exercise,
    });

    this.exercisesSharedCollection = this.exerciseService.addExerciseToCollectionIfMissing(
      this.exercisesSharedCollection,
      exerciseTool.exercise
    );
  }

  protected loadRelationshipsOptions(): void {
    this.exerciseService
      .query()
      .pipe(map((res: HttpResponse<IExercise[]>) => res.body ?? []))
      .pipe(
        map((exercises: IExercise[]) =>
          this.exerciseService.addExerciseToCollectionIfMissing(exercises, this.editForm.get('exercise')!.value)
        )
      )
      .subscribe((exercises: IExercise[]) => (this.exercisesSharedCollection = exercises));
  }

  protected createFromForm(): IExerciseTool {
    return {
      ...new ExerciseTool(),
      id: this.editForm.get(['id'])!.value,
      uuid: this.editForm.get(['uuid'])!.value,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      exercise: this.editForm.get(['exercise'])!.value,
    };
  }
}
