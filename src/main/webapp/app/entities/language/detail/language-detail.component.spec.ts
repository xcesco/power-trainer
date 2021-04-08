import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LanguageDetailComponent } from './language-detail.component';

describe('Component Tests', () => {
  describe('Language Management Detail Component', () => {
    let comp: LanguageDetailComponent;
    let fixture: ComponentFixture<LanguageDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [LanguageDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ language: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(LanguageDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LanguageDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load language on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.language).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
