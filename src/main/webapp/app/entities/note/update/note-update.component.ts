import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { INote, Note } from '../note.model';
import { NoteService } from '../service/note.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IExercise } from 'app/entities/exercise/exercise.model';
import { ExerciseService } from 'app/entities/exercise/service/exercise.service';

@Component({
  selector: 'jhi-note-update',
  templateUrl: './note-update.component.html',
})
export class NoteUpdateComponent implements OnInit {
  isSaving = false;

  exercisesSharedCollection: IExercise[] = [];

  editForm = this.fb.group({
    id: [],
    uuid: [null, [Validators.required]],
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
    protected noteService: NoteService,
    protected exerciseService: ExerciseService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ note }) => {
      this.updateForm(note);

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
    const note = this.createFromForm();
    if (note.id !== undefined) {
      this.subscribeToSaveResponse(this.noteService.update(note));
    } else {
      this.subscribeToSaveResponse(this.noteService.create(note));
    }
  }

  trackExerciseById(index: number, item: IExercise): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INote>>): void {
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

  protected updateForm(note: INote): void {
    this.editForm.patchValue({
      id: note.id,
      uuid: note.uuid,
      type: note.type,
      url: note.url,
      image: note.image,
      imageContentType: note.imageContentType,
      description: note.description,
      exercise: note.exercise,
    });

    this.exercisesSharedCollection = this.exerciseService.addExerciseToCollectionIfMissing(this.exercisesSharedCollection, note.exercise);
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

  protected createFromForm(): INote {
    return {
      ...new Note(),
      id: this.editForm.get(['id'])!.value,
      uuid: this.editForm.get(['uuid'])!.value,
      type: this.editForm.get(['type'])!.value,
      url: this.editForm.get(['url'])!.value,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
      description: this.editForm.get(['description'])!.value,
      exercise: this.editForm.get(['exercise'])!.value,
    };
  }
}
