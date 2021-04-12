import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ILanguage, Language } from '../language.model';
import { LanguageService } from '../service/language.service';

@Component({
  selector: 'jhi-language-update',
  templateUrl: './language-update.component.html',
})
export class LanguageUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    code: [null, [Validators.required]],
    name: [null, [Validators.required]],
  });

  constructor(protected languageService: LanguageService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ language }) => {
      this.updateForm(language);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const language = this.createFromForm();
    if (language.id !== undefined) {
      this.subscribeToSaveResponse(this.languageService.update(language));
    } else {
      this.subscribeToSaveResponse(this.languageService.create(language));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILanguage>>): void {
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

  protected updateForm(language: ILanguage): void {
    this.editForm.patchValue({
      id: language.id,
      code: language.code,
      name: language.name,
    });
  }

  protected createFromForm(): ILanguage {
    return {
      ...new Language(),
      id: this.editForm.get(['id'])!.value,
      code: this.editForm.get(['code'])!.value,
      name: this.editForm.get(['name'])!.value,
    };
  }
}
