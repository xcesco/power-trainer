import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WorkoutSheetExerciseDetailComponent } from './workout-sheet-exercise-detail.component';

describe('Component Tests', () => {
  describe('WorkoutSheetExercise Management Detail Component', () => {
    let comp: WorkoutSheetExerciseDetailComponent;
    let fixture: ComponentFixture<WorkoutSheetExerciseDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [WorkoutSheetExerciseDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ workoutSheetExercise: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(WorkoutSheetExerciseDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(WorkoutSheetExerciseDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load workoutSheetExercise on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.workoutSheetExercise).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
