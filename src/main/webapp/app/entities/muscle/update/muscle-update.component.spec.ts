jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MuscleService } from '../service/muscle.service';
import { IMuscle, Muscle } from '../muscle.model';

import { MuscleUpdateComponent } from './muscle-update.component';

describe('Component Tests', () => {
  describe('Muscle Management Update Component', () => {
    let comp: MuscleUpdateComponent;
    let fixture: ComponentFixture<MuscleUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let muscleService: MuscleService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MuscleUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(MuscleUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MuscleUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      muscleService = TestBed.inject(MuscleService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const muscle: IMuscle = { id: 456 };

        activatedRoute.data = of({ muscle });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(muscle));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const muscle = { id: 123 };
        spyOn(muscleService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ muscle });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: muscle }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(muscleService.update).toHaveBeenCalledWith(muscle);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const muscle = new Muscle();
        spyOn(muscleService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ muscle });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: muscle }));
        saveSubject.complete();

        // THEN
        expect(muscleService.create).toHaveBeenCalledWith(muscle);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const muscle = { id: 123 };
        spyOn(muscleService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ muscle });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(muscleService.update).toHaveBeenCalledWith(muscle);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
