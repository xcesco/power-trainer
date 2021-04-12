import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TranslationDetailComponent } from './translation-detail.component';

describe('Component Tests', () => {
  describe('Translation Management Detail Component', () => {
    let comp: TranslationDetailComponent;
    let fixture: ComponentFixture<TranslationDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [TranslationDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ translation: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(TranslationDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TranslationDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load translation on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.translation).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
