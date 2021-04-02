jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { Muscle2ExerciseService } from '../service/muscle-2-exercise.service';
import { IMuscle2Exercise, Muscle2Exercise } from '../muscle-2-exercise.model';

import { Muscle2ExerciseUpdateComponent } from './muscle-2-exercise-update.component';

describe('Component Tests', () => {
  describe('Muscle2Exercise Management Update Component', () => {
    let comp: Muscle2ExerciseUpdateComponent;
    let fixture: ComponentFixture<Muscle2ExerciseUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let muscle2ExerciseService: Muscle2ExerciseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [Muscle2ExerciseUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(Muscle2ExerciseUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(Muscle2ExerciseUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      muscle2ExerciseService = TestBed.inject(Muscle2ExerciseService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const muscle2Exercise: IMuscle2Exercise = { id: 456 };

        activatedRoute.data = of({ muscle2Exercise });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(muscle2Exercise));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const muscle2Exercise = { id: 123 };
        spyOn(muscle2ExerciseService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ muscle2Exercise });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: muscle2Exercise }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(muscle2ExerciseService.update).toHaveBeenCalledWith(muscle2Exercise);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const muscle2Exercise = new Muscle2Exercise();
        spyOn(muscle2ExerciseService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ muscle2Exercise });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: muscle2Exercise }));
        saveSubject.complete();

        // THEN
        expect(muscle2ExerciseService.create).toHaveBeenCalledWith(muscle2Exercise);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const muscle2Exercise = { id: 123 };
        spyOn(muscle2ExerciseService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ muscle2Exercise });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(muscle2ExerciseService.update).toHaveBeenCalledWith(muscle2Exercise);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
