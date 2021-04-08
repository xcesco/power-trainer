jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { WorkoutSheetExerciseService } from '../service/workout-sheet-exercise.service';
import { IWorkoutSheetExercise, WorkoutSheetExercise } from '../workout-sheet-exercise.model';
import { IWorkoutSheet } from 'app/entities/workout-sheet/workout-sheet.model';
import { WorkoutSheetService } from 'app/entities/workout-sheet/service/workout-sheet.service';

import { WorkoutSheetExerciseUpdateComponent } from './workout-sheet-exercise-update.component';

describe('Component Tests', () => {
  describe('WorkoutSheetExercise Management Update Component', () => {
    let comp: WorkoutSheetExerciseUpdateComponent;
    let fixture: ComponentFixture<WorkoutSheetExerciseUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let workoutSheetExerciseService: WorkoutSheetExerciseService;
    let workoutSheetService: WorkoutSheetService;

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
      workoutSheetService = TestBed.inject(WorkoutSheetService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call WorkoutSheet query and add missing value', () => {
        const workoutSheetExercise: IWorkoutSheetExercise = { id: 456 };
        const workoutSheet: IWorkoutSheet = { id: 4799 };
        workoutSheetExercise.workoutSheet = workoutSheet;

        const workoutSheetCollection: IWorkoutSheet[] = [{ id: 47627 }];
        spyOn(workoutSheetService, 'query').and.returnValue(of(new HttpResponse({ body: workoutSheetCollection })));
        const additionalWorkoutSheets = [workoutSheet];
        const expectedCollection: IWorkoutSheet[] = [...additionalWorkoutSheets, ...workoutSheetCollection];
        spyOn(workoutSheetService, 'addWorkoutSheetToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ workoutSheetExercise });
        comp.ngOnInit();

        expect(workoutSheetService.query).toHaveBeenCalled();
        expect(workoutSheetService.addWorkoutSheetToCollectionIfMissing).toHaveBeenCalledWith(
          workoutSheetCollection,
          ...additionalWorkoutSheets
        );
        expect(comp.workoutSheetsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const workoutSheetExercise: IWorkoutSheetExercise = { id: 456 };
        const workoutSheet: IWorkoutSheet = { id: 46541 };
        workoutSheetExercise.workoutSheet = workoutSheet;

        activatedRoute.data = of({ workoutSheetExercise });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(workoutSheetExercise));
        expect(comp.workoutSheetsSharedCollection).toContain(workoutSheet);
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

    describe('Tracking relationships identifiers', () => {
      describe('trackWorkoutSheetById', () => {
        it('Should return tracked WorkoutSheet primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackWorkoutSheetById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
