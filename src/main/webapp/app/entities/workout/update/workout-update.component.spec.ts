jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { WorkoutService } from '../service/workout.service';
import { IWorkout, Workout } from '../workout.model';

import { WorkoutUpdateComponent } from './workout-update.component';

describe('Component Tests', () => {
  describe('Workout Management Update Component', () => {
    let comp: WorkoutUpdateComponent;
    let fixture: ComponentFixture<WorkoutUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let workoutService: WorkoutService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [WorkoutUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(WorkoutUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(WorkoutUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      workoutService = TestBed.inject(WorkoutService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const workout: IWorkout = { id: 456 };

        activatedRoute.data = of({ workout });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(workout));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const workout = { id: 123 };
        spyOn(workoutService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ workout });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: workout }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(workoutService.update).toHaveBeenCalledWith(workout);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const workout = new Workout();
        spyOn(workoutService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ workout });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: workout }));
        saveSubject.complete();

        // THEN
        expect(workoutService.create).toHaveBeenCalledWith(workout);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const workout = { id: 123 };
        spyOn(workoutService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ workout });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(workoutService.update).toHaveBeenCalledWith(workout);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
