jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ExerciseResourceService } from '../service/exercise-resource.service';
import { IExerciseResource, ExerciseResource } from '../exercise-resource.model';
import { IExercise } from 'app/entities/exercise/exercise.model';
import { ExerciseService } from 'app/entities/exercise/service/exercise.service';

import { ExerciseResourceUpdateComponent } from './exercise-resource-update.component';

describe('Component Tests', () => {
  describe('ExerciseResource Management Update Component', () => {
    let comp: ExerciseResourceUpdateComponent;
    let fixture: ComponentFixture<ExerciseResourceUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let exerciseResourceService: ExerciseResourceService;
    let exerciseService: ExerciseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ExerciseResourceUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ExerciseResourceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ExerciseResourceUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      exerciseResourceService = TestBed.inject(ExerciseResourceService);
      exerciseService = TestBed.inject(ExerciseService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Exercise query and add missing value', () => {
        const exerciseResource: IExerciseResource = { id: 456 };
        const exercise: IExercise = { id: 2309 };
        exerciseResource.exercise = exercise;

        const exerciseCollection: IExercise[] = [{ id: 67261 }];
        spyOn(exerciseService, 'query').and.returnValue(of(new HttpResponse({ body: exerciseCollection })));
        const additionalExercises = [exercise];
        const expectedCollection: IExercise[] = [...additionalExercises, ...exerciseCollection];
        spyOn(exerciseService, 'addExerciseToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ exerciseResource });
        comp.ngOnInit();

        expect(exerciseService.query).toHaveBeenCalled();
        expect(exerciseService.addExerciseToCollectionIfMissing).toHaveBeenCalledWith(exerciseCollection, ...additionalExercises);
        expect(comp.exercisesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const exerciseResource: IExerciseResource = { id: 456 };
        const exercise: IExercise = { id: 18995 };
        exerciseResource.exercise = exercise;

        activatedRoute.data = of({ exerciseResource });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(exerciseResource));
        expect(comp.exercisesSharedCollection).toContain(exercise);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const exerciseResource = { id: 123 };
        spyOn(exerciseResourceService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ exerciseResource });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: exerciseResource }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(exerciseResourceService.update).toHaveBeenCalledWith(exerciseResource);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const exerciseResource = new ExerciseResource();
        spyOn(exerciseResourceService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ exerciseResource });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: exerciseResource }));
        saveSubject.complete();

        // THEN
        expect(exerciseResourceService.create).toHaveBeenCalledWith(exerciseResource);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const exerciseResource = { id: 123 };
        spyOn(exerciseResourceService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ exerciseResource });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(exerciseResourceService.update).toHaveBeenCalledWith(exerciseResource);
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
