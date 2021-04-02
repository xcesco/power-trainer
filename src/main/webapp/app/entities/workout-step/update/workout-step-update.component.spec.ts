jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { WorkoutStepService } from '../service/workout-step.service';
import { IWorkoutStep, WorkoutStep } from '../workout-step.model';

import { WorkoutStepUpdateComponent } from './workout-step-update.component';

describe('Component Tests', () => {
  describe('WorkoutStep Management Update Component', () => {
    let comp: WorkoutStepUpdateComponent;
    let fixture: ComponentFixture<WorkoutStepUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let workoutStepService: WorkoutStepService;

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

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const workoutStep: IWorkoutStep = { id: 456 };

        activatedRoute.data = of({ workoutStep });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(workoutStep));
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
  });
});
