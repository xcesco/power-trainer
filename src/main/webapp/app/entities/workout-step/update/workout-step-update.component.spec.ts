jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { WorkoutStepService } from '../service/workout-step.service';
import { IWorkoutStep, WorkoutStep } from '../workout-step.model';
import { IWorkout } from 'app/entities/workout/workout.model';
import { WorkoutService } from 'app/entities/workout/service/workout.service';

import { WorkoutStepUpdateComponent } from './workout-step-update.component';

describe('Component Tests', () => {
  describe('WorkoutStep Management Update Component', () => {
    let comp: WorkoutStepUpdateComponent;
    let fixture: ComponentFixture<WorkoutStepUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let workoutStepService: WorkoutStepService;
    let workoutService: WorkoutService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [WorkoutStepUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(WorkoutStepUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(WorkoutStepUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      workoutStepService = TestBed.inject(WorkoutStepService);
      workoutService = TestBed.inject(WorkoutService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Workout query and add missing value', () => {
        const workoutStep: IWorkoutStep = { id: 456 };
        const workout: IWorkout = { id: 18134 };
        workoutStep.workout = workout;

        const workoutCollection: IWorkout[] = [{ id: 71273 }];
        spyOn(workoutService, 'query').and.returnValue(of(new HttpResponse({ body: workoutCollection })));
        const additionalWorkouts = [workout];
        const expectedCollection: IWorkout[] = [...additionalWorkouts, ...workoutCollection];
        spyOn(workoutService, 'addWorkoutToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ workoutStep });
        comp.ngOnInit();

        expect(workoutService.query).toHaveBeenCalled();
        expect(workoutService.addWorkoutToCollectionIfMissing).toHaveBeenCalledWith(workoutCollection, ...additionalWorkouts);
        expect(comp.workoutsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const workoutStep: IWorkoutStep = { id: 456 };
        const workout: IWorkout = { id: 87941 };
        workoutStep.workout = workout;

        activatedRoute.data = of({ workoutStep });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(workoutStep));
        expect(comp.workoutsSharedCollection).toContain(workout);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const workoutStep = { id: 123 };
        spyOn(workoutStepService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ workoutStep });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: workoutStep }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(workoutStepService.update).toHaveBeenCalledWith(workoutStep);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const workoutStep = new WorkoutStep();
        spyOn(workoutStepService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ workoutStep });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: workoutStep }));
        saveSubject.complete();

        // THEN
        expect(workoutStepService.create).toHaveBeenCalledWith(workoutStep);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const workoutStep = { id: 123 };
        spyOn(workoutStepService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ workoutStep });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(workoutStepService.update).toHaveBeenCalledWith(workoutStep);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackWorkoutById', () => {
        it('Should return tracked Workout primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackWorkoutById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
