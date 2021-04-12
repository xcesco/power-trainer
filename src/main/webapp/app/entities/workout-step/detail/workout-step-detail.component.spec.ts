import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WorkoutStepDetailComponent } from './workout-step-detail.component';

describe('Component Tests', () => {
  describe('WorkoutStep Management Detail Component', () => {
    let comp: WorkoutStepDetailComponent;
    let fixture: ComponentFixture<WorkoutStepDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [WorkoutStepDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ workoutStep: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(WorkoutStepDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(WorkoutStepDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load workoutStep on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.workoutStep).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
