jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MeasureTypeService } from '../service/measure-type.service';
import { IMeasureType, MeasureType } from '../measure-type.model';

import { MeasureTypeUpdateComponent } from './measure-type-update.component';

describe('Component Tests', () => {
  describe('MeasureType Management Update Component', () => {
    let comp: MeasureTypeUpdateComponent;
    let fixture: ComponentFixture<MeasureTypeUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let measureTypeService: MeasureTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MeasureTypeUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(MeasureTypeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MeasureTypeUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      measureTypeService = TestBed.inject(MeasureTypeService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const measureType: IMeasureType = { id: 456 };

        activatedRoute.data = of({ measureType });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(measureType));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const measureType = { id: 123 };
        spyOn(measureTypeService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ measureType });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: measureType }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(measureTypeService.update).toHaveBeenCalledWith(measureType);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const measureType = new MeasureType();
        spyOn(measureTypeService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ measureType });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: measureType }));
        saveSubject.complete();

        // THEN
        expect(measureTypeService.create).toHaveBeenCalledWith(measureType);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const measureType = { id: 123 };
        spyOn(measureTypeService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ measureType });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(measureTypeService.update).toHaveBeenCalledWith(measureType);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
