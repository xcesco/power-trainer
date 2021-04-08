import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ITranslation, Translation } from '../translation.model';
import { TranslationService } from '../service/translation.service';
import { ILanguage } from 'app/entities/language/language.model';
import { LanguageService } from 'app/entities/language/service/language.service';

@Component({
  selector: 'jhi-translation-update',
  templateUrl: './translation-update.component.html',
})
export class TranslationUpdateComponent implements OnInit {
  isSaving = false;

  languagesSharedCollection: ILanguage[] = [];

  editForm = this.fb.group({
    id: [],
    entityType: [null, [Validators.required]],
    entityUuid: [null, [Validators.required]],
    value: [null, [Validators.required]],
    entityField: [null, [Validators.required]],
    language: [],
  });

  constructor(
    protected translationService: TranslationService,
    protected languageService: LanguageService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ translation }) => {
      this.updateForm(translation);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const translation = this.createFromForm();
    if (translation.id !== undefined) {
      this.subscribeToSaveResponse(this.translationService.update(translation));
    } else {
      this.subscribeToSaveResponse(this.translationService.create(translation));
    }
  }

  trackLanguageById(index: number, item: ILanguage): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITranslation>>): void {
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

  protected updateForm(translation: ITranslation): void {
    this.editForm.patchValue({
      id: translation.id,
      entityType: translation.entityType,
      entityUuid: translation.entityUuid,
      value: translation.value,
      entityField: translation.entityField,
      language: translation.language,
    });

    this.languagesSharedCollection = this.languageService.addLanguageToCollectionIfMissing(
      this.languagesSharedCollection,
      translation.language
    );
  }

  protected loadRelationshipsOptions(): void {
    this.languageService
      .query()
      .pipe(map((res: HttpResponse<ILanguage[]>) => res.body ?? []))
      .pipe(
        map((languages: ILanguage[]) =>
          this.languageService.addLanguageToCollectionIfMissing(languages, this.editForm.get('language')!.value)
        )
      )
      .subscribe((languages: ILanguage[]) => (this.languagesSharedCollection = languages));
  }

  protected createFromForm(): ITranslation {
    return {
      ...new Translation(),
      id: this.editForm.get(['id'])!.value,
      entityType: this.editForm.get(['entityType'])!.value,
      entityUuid: this.editForm.get(['entityUuid'])!.value,
      value: this.editForm.get(['value'])!.value,
      entityField: this.editForm.get(['entityField'])!.value,
      language: this.editForm.get(['language'])!.value,
    };
  }
}
