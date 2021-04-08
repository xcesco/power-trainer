jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ExerciseToolService } from '../service/exercise-tool.service';
import { IExerciseTool, ExerciseTool } from '../exercise-tool.model';
import { IExercise } from 'app/entities/exercise/exercise.model';
import { ExerciseService } from 'app/entities/exercise/service/exercise.service';

import { ExerciseToolUpdateComponent } from './exercise-tool-update.component';

describe('Component Tests', () => {
  describe('ExerciseTool Management Update Component', () => {
    let comp: ExerciseToolUpdateComponent;
    let fixture: ComponentFixture<ExerciseToolUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let exerciseToolService: ExerciseToolService;
    let exerciseService: ExerciseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ExerciseToolUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ExerciseToolUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ExerciseToolUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      exerciseToolService = TestBed.inject(ExerciseToolService);
      exerciseService = TestBed.inject(ExerciseService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Exercise query and add missing value', () => {
        const exerciseTool: IExerciseTool = { id: 456 };
        const exercise: IExercise = { id: 2309 };
        exerciseTool.exercise = exercise;

        const exerciseCollection: IExercise[] = [{ id: 67261 }];
        spyOn(exerciseService, 'query').and.returnValue(of(new HttpResponse({ body: exerciseCollection })));
        const additionalExercises = [exercise];
        const expectedCollection: IExercise[] = [...additionalExercises, ...exerciseCollection];
        spyOn(exerciseService, 'addExerciseToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ exerciseTool });
        comp.ngOnInit();

        expect(exerciseService.query).toHaveBeenCalled();
        expect(exerciseService.addExerciseToCollectionIfMissing).toHaveBeenCalledWith(exerciseCollection, ...additionalExercises);
        expect(comp.exercisesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const exerciseTool: IExerciseTool = { id: 456 };
        const exercise: IExercise = { id: 18995 };
        exerciseTool.exercise = exercise;

        activatedRoute.data = of({ exerciseTool });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(exerciseTool));
        expect(comp.exercisesSharedCollection).toContain(exercise);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const exerciseTool = { id: 123 };
        spyOn(exerciseToolService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ exerciseTool });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: exerciseTool }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(exerciseToolService.update).toHaveBeenCalledWith(exerciseTool);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const exerciseTool = new ExerciseTool();
        spyOn(exerciseToolService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ exerciseTool });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: exerciseTool }));
        saveSubject.complete();

        // THEN
        expect(exerciseToolService.create).toHaveBeenCalledWith(exerciseTool);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const exerciseTool = { id: 123 };
        spyOn(exerciseToolService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ exerciseTool });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(exerciseToolService.update).toHaveBeenCalledWith(exerciseTool);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackExerciseById', () => {
        it('Should return tracked Exercise primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackExerciseById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
