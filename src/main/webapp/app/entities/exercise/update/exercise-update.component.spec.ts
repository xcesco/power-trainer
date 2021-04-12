jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ExerciseService } from '../service/exercise.service';
import { IExercise, Exercise } from '../exercise.model';
import { IMuscle } from 'app/entities/muscle/muscle.model';
import { MuscleService } from 'app/entities/muscle/service/muscle.service';
import { IExerciseTool } from 'app/entities/exercise-tool/exercise-tool.model';
import { ExerciseToolService } from 'app/entities/exercise-tool/service/exercise-tool.service';

import { ExerciseUpdateComponent } from './exercise-update.component';

describe('Component Tests', () => {
  describe('Exercise Management Update Component', () => {
    let comp: ExerciseUpdateComponent;
    let fixture: ComponentFixture<ExerciseUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let exerciseService: ExerciseService;
    let muscleService: MuscleService;
    let exerciseToolService: ExerciseToolService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ExerciseUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ExerciseUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ExerciseUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      exerciseService = TestBed.inject(ExerciseService);
      muscleService = TestBed.inject(MuscleService);
      exerciseToolService = TestBed.inject(ExerciseToolService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Muscle query and add missing value', () => {
        const exercise: IExercise = { id: 456 };
        const muscles: IMuscle[] = [{ id: 56589 }];
        exercise.muscles = muscles;

        const muscleCollection: IMuscle[] = [{ id: 29713 }];
        spyOn(muscleService, 'query').and.returnValue(of(new HttpResponse({ body: muscleCollection })));
        const additionalMuscles = [...muscles];
        const expectedCollection: IMuscle[] = [...additionalMuscles, ...muscleCollection];
        spyOn(muscleService, 'addMuscleToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ exercise });
        comp.ngOnInit();

        expect(muscleService.query).toHaveBeenCalled();
        expect(muscleService.addMuscleToCollectionIfMissing).toHaveBeenCalledWith(muscleCollection, ...additionalMuscles);
        expect(comp.musclesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call ExerciseTool query and add missing value', () => {
        const exercise: IExercise = { id: 456 };
        const exerciseTools: IExerciseTool[] = [{ id: 42700 }];
        exercise.exerciseTools = exerciseTools;

        const exerciseToolCollection: IExerciseTool[] = [{ id: 87393 }];
        spyOn(exerciseToolService, 'query').and.returnValue(of(new HttpResponse({ body: exerciseToolCollection })));
        const additionalExerciseTools = [...exerciseTools];
        const expectedCollection: IExerciseTool[] = [...additionalExerciseTools, ...exerciseToolCollection];
        spyOn(exerciseToolService, 'addExerciseToolToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ exercise });
        comp.ngOnInit();

        expect(exerciseToolService.query).toHaveBeenCalled();
        expect(exerciseToolService.addExerciseToolToCollectionIfMissing).toHaveBeenCalledWith(
          exerciseToolCollection,
          ...additionalExerciseTools
        );
        expect(comp.exerciseToolsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const exercise: IExercise = { id: 456 };
        const muscles: IMuscle = { id: 32949 };
        exercise.muscles = [muscles];
        const exerciseTools: IExerciseTool = { id: 72143 };
        exercise.exerciseTools = [exerciseTools];

        activatedRoute.data = of({ exercise });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(exercise));
        expect(comp.musclesSharedCollection).toContain(muscles);
        expect(comp.exerciseToolsSharedCollection).toContain(exerciseTools);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const exercise = { id: 123 };
        spyOn(exerciseService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ exercise });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: exercise }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(exerciseService.update).toHaveBeenCalledWith(exercise);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const exercise = new Exercise();
        spyOn(exerciseService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ exercise });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: exercise }));
        saveSubject.complete();

        // THEN
        expect(exerciseService.create).toHaveBeenCalledWith(exercise);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const exercise = { id: 123 };
        spyOn(exerciseService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ exercise });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(exerciseService.update).toHaveBeenCalledWith(exercise);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackMuscleById', () => {
        it('Should return tracked Muscle primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackMuscleById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackExerciseToolById', () => {
        it('Should return tracked ExerciseTool primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackExerciseToolById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedMuscle', () => {
        it('Should return option if no Muscle is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedMuscle(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Muscle for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedMuscle(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Muscle is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedMuscle(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });

      describe('getSelectedExerciseTool', () => {
        it('Should return option if no ExerciseTool is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedExerciseTool(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected ExerciseTool for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedExerciseTool(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this ExerciseTool is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedExerciseTool(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
