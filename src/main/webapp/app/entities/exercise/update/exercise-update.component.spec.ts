jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ExerciseService } from '../service/exercise.service';
import { IExercise, Exercise } from '../exercise.model';

import { ExerciseUpdateComponent } from './exercise-update.component';

describe('Component Tests', () => {
  describe('Exercise Management Update Component', () => {
    let comp: ExerciseUpdateComponent;
    let fixture: ComponentFixture<ExerciseUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let exerciseService: ExerciseService;

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

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const exercise: IExercise = { id: 456 };

        activatedRoute.data = of({ exercise });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(exercise));
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
  });
});
