import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITranslation } from '../translation.model';

@Component({
  selector: 'jhi-translation-detail',
  templateUrl: './translation-detail.component.html',
})
export class TranslationDetailComponent implements OnInit {
  translation: ITranslation | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ translation }) => {
      this.translation = translation;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
