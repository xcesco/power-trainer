jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TranslationService } from '../service/translation.service';
import { ITranslation, Translation } from '../translation.model';
import { ILanguage } from 'app/entities/language/language.model';
import { LanguageService } from 'app/entities/language/service/language.service';

import { TranslationUpdateComponent } from './translation-update.component';

describe('Component Tests', () => {
  describe('Translation Management Update Component', () => {
    let comp: TranslationUpdateComponent;
    let fixture: ComponentFixture<TranslationUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let translationService: TranslationService;
    let languageService: LanguageService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TranslationUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TranslationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TranslationUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      translationService = TestBed.inject(TranslationService);
      languageService = TestBed.inject(LanguageService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Language query and add missing value', () => {
        const translation: ITranslation = { id: 456 };
        const language: ILanguage = { id: 42550 };
        translation.language = language;

        const languageCollection: ILanguage[] = [{ id: 15717 }];
        spyOn(languageService, 'query').and.returnValue(of(new HttpResponse({ body: languageCollection })));
        const additionalLanguages = [language];
        const expectedCollection: ILanguage[] = [...additionalLanguages, ...languageCollection];
        spyOn(languageService, 'addLanguageToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ translation });
        comp.ngOnInit();

        expect(languageService.query).toHaveBeenCalled();
        expect(languageService.addLanguageToCollectionIfMissing).toHaveBeenCalledWith(languageCollection, ...additionalLanguages);
        expect(comp.languagesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const translation: ITranslation = { id: 456 };
        const language: ILanguage = { id: 34998 };
        translation.language = language;

        activatedRoute.data = of({ translation });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(translation));
        expect(comp.languagesSharedCollection).toContain(language);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const translation = { id: 123 };
        spyOn(translationService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ translation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: translation }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(translationService.update).toHaveBeenCalledWith(translation);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const translation = new Translation();
        spyOn(translationService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ translation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: translation }));
        saveSubject.complete();

        // THEN
        expect(translationService.create).toHaveBeenCalledWith(translation);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const translation = { id: 123 };
        spyOn(translationService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ translation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(translationService.update).toHaveBeenCalledWith(translation);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackLanguageById', () => {
        it('Should return tracked Language primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackLanguageById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
