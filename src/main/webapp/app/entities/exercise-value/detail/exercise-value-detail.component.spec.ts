import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ExerciseValueDetailComponent } from './exercise-value-detail.component';

describe('Component Tests', () => {
  describe('ExerciseValue Management Detail Component', () => {
    let comp: ExerciseValueDetailComponent;
    let fixture: ComponentFixture<ExerciseValueDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ExerciseValueDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ exerciseValue: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ExerciseValueDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ExerciseValueDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load exerciseValue on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.exerciseValue).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
