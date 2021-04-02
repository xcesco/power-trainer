jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { WorkoutSheetExerciseService } from '../service/workout-sheet-exercise.service';
import { IWorkoutSheetExercise, WorkoutSheetExercise } from '../workout-sheet-exercise.model';

import { WorkoutSheetExerciseUpdateComponent } from './workout-sheet-exercise-update.component';

describe('Component Tests', () => {
  describe('WorkoutSheetExercise Management Update Component', () => {
    let comp: WorkoutSheetExerciseUpdateComponent;
    let fixture: ComponentFixture<WorkoutSheetExerciseUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let workoutSheetExerciseService: WorkoutSheetExerciseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [WorkoutSheetExerciseUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(WorkoutSheetExerciseUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(WorkoutSheetExerciseUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      workoutSheetExerciseService = TestBed.inject(WorkoutSheetExerciseService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const workoutSheetExercise: IWorkoutSheetExercise = { id: 456 };

        activatedRoute.data = of({ workoutSheetExercise });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(workoutSheetExercise));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const workoutSheetExercise = { id: 123 };
        spyOn(workoutSheetExerciseService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ workoutSheetExercise });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: workoutSheetExercise }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(workoutSheetExerciseService.update).toHaveBeenCalledWith(workoutSheetExercise);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const workoutSheetExercise = new WorkoutSheetExercise();
        spyOn(workoutSheetExerciseService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ workoutSheetExercise });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: workoutSheetExercise }));
        saveSubject.complete();

        // THEN
        expect(workoutSheetExerciseService.create).toHaveBeenCalledWith(workoutSheetExercise);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const workoutSheetExercise = { id: 123 };
        spyOn(workoutSheetExerciseService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ workoutSheetExercise });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(workoutSheetExerciseService.update).toHaveBeenCalledWith(workoutSheetExercise);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
