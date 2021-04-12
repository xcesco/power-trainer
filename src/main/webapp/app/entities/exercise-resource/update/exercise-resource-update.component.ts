import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IExerciseResource, ExerciseResource } from '../exercise-resource.model';
import { ExerciseResourceService } from '../service/exercise-resource.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IExercise } from 'app/entities/exercise/exercise.model';
import { ExerciseService } from 'app/entities/exercise/service/exercise.service';

@Component({
  selector: 'jhi-exercise-resource-update',
  templateUrl: './exercise-resource-update.component.html',
})
export class ExerciseResourceUpdateComponent implements OnInit {
  isSaving = false;

  exercisesSharedCollection: IExercise[] = [];

  editForm = this.fb.group({
    id: [],
    uuid: [null, [Validators.required]],
    order: [],
    type: [null, [Validators.required]],
    url: [],
    image: [],
    imageContentType: [],
    description: [],
    exercise: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected exerciseResourceService: ExerciseResourceService,
    protected exerciseService: ExerciseService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ exerciseResource }) => {
      this.updateForm(exerciseResource);

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
    const exerciseResource = this.createFromForm();
    if (exerciseResource.id !== undefined) {
      this.subscribeToSaveResponse(this.exerciseResourceService.update(exerciseResource));
    } else {
      this.subscribeToSaveResponse(this.exerciseResourceService.create(exerciseResource));
    }
  }

  trackExerciseById(index: number, item: IExercise): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExerciseResource>>): void {
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

  protected updateForm(exerciseResource: IExerciseResource): void {
    this.editForm.patchValue({
      id: exerciseResource.id,
      uuid: exerciseResource.uuid,
      order: exerciseResource.order,
      type: exerciseResource.type,
      url: exerciseResource.url,
      image: exerciseResource.image,
      imageContentType: exerciseResource.imageContentType,
      description: exerciseResource.description,
      exercise: exerciseResource.exercise,
    });

    this.exercisesSharedCollection = this.exerciseService.addExerciseToCollectionIfMissing(
      this.exercisesSharedCollection,
      exerciseResource.exercise
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

  protected createFromForm(): IExerciseResource {
    return {
      ...new ExerciseResource(),
      id: this.editForm.get(['id'])!.value,
      uuid: this.editForm.get(['uuid'])!.value,
      order: this.editForm.get(['order'])!.value,
      type: this.editForm.get(['type'])!.value,
      url: this.editForm.get(['url'])!.value,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
      description: this.editForm.get(['description'])!.value,
      exercise: this.editForm.get(['exercise'])!.value,
    };
  }
}
