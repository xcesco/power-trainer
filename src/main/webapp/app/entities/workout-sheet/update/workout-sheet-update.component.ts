import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IWorkoutSheet, WorkoutSheet } from '../workout-sheet.model';
import { WorkoutSheetService } from '../service/workout-sheet.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-workout-sheet-update',
  templateUrl: './workout-sheet-update.component.html',
})
export class WorkoutSheetUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    uuid: [null, [Validators.required]],
    name: [null, [Validators.required]],
    image: [],
    imageContentType: [],
    description: [],
    owner: [],
    prepareTime: [],
    coolDownTime: [],
    cycles: [],
    cycleRestTime: [],
    set: [],
    setRestTime: [],
    type: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected workoutSheetService: WorkoutSheetService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ workoutSheet }) => {
      this.updateForm(workoutSheet);
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
    const workoutSheet = this.createFromForm();
    if (workoutSheet.id !== undefined) {
      this.subscribeToSaveResponse(this.workoutSheetService.update(workoutSheet));
    } else {
      this.subscribeToSaveResponse(this.workoutSheetService.create(workoutSheet));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWorkoutSheet>>): void {
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

  protected updateForm(workoutSheet: IWorkoutSheet): void {
    this.editForm.patchValue({
      id: workoutSheet.id,
      uuid: workoutSheet.uuid,
      name: workoutSheet.name,
      image: workoutSheet.image,
      imageContentType: workoutSheet.imageContentType,
      description: workoutSheet.description,
      owner: workoutSheet.owner,
      prepareTime: workoutSheet.prepareTime,
      coolDownTime: workoutSheet.coolDownTime,
      cycles: workoutSheet.cycles,
      cycleRestTime: workoutSheet.cycleRestTime,
      set: workoutSheet.set,
      setRestTime: workoutSheet.setRestTime,
      type: workoutSheet.type,
    });
  }

  protected createFromForm(): IWorkoutSheet {
    return {
      ...new WorkoutSheet(),
      id: this.editForm.get(['id'])!.value,
      uuid: this.editForm.get(['uuid'])!.value,
      name: this.editForm.get(['name'])!.value,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
      description: this.editForm.get(['description'])!.value,
      owner: this.editForm.get(['owner'])!.value,
      prepareTime: this.editForm.get(['prepareTime'])!.value,
      coolDownTime: this.editForm.get(['coolDownTime'])!.value,
      cycles: this.editForm.get(['cycles'])!.value,
      cycleRestTime: this.editForm.get(['cycleRestTime'])!.value,
      set: this.editForm.get(['set'])!.value,
      setRestTime: this.editForm.get(['setRestTime'])!.value,
      type: this.editForm.get(['type'])!.value,
    };
  }
}
