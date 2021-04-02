jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MeasureValueService } from '../service/measure-value.service';
import { IMeasureValue, MeasureValue } from '../measure-value.model';

import { MeasureValueUpdateComponent } from './measure-value-update.component';

describe('Component Tests', () => {
  describe('MeasureValue Management Update Component', () => {
    let comp: MeasureValueUpdateComponent;
    let fixture: ComponentFixture<MeasureValueUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let measureValueService: MeasureValueService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MeasureValueUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(MeasureValueUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MeasureValueUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      measureValueService = TestBed.inject(MeasureValueService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const measureValue: IMeasureValue = { id: 456 };

        activatedRoute.data = of({ measureValue });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(measureValue));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const measureValue = { id: 123 };
        spyOn(measureValueService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ measureValue });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: measureValue }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(measureValueService.update).toHaveBeenCalledWith(measureValue);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const measureValue = new MeasureValue();
        spyOn(measureValueService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ measureValue });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: measureValue }));
        saveSubject.complete();

        // THEN
        expect(measureValueService.create).toHaveBeenCalledWith(measureValue);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const measureValue = { id: 123 };
        spyOn(measureValueService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ measureValue });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(measureValueService.update).toHaveBeenCalledWith(measureValue);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
