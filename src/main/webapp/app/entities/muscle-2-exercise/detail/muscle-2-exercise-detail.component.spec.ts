import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Muscle2ExerciseDetailComponent } from './muscle-2-exercise-detail.component';

describe('Component Tests', () => {
  describe('Muscle2Exercise Management Detail Component', () => {
    let comp: Muscle2ExerciseDetailComponent;
    let fixture: ComponentFixture<Muscle2ExerciseDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [Muscle2ExerciseDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ muscle2Exercise: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(Muscle2ExerciseDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(Muscle2ExerciseDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load muscle2Exercise on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.muscle2Exercise).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
