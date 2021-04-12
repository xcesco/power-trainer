jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MisurationTypeService } from '../service/misuration-type.service';
import { IMisurationType, MisurationType } from '../misuration-type.model';

import { MisurationTypeUpdateComponent } from './misuration-type-update.component';

describe('Component Tests', () => {
  describe('MisurationType Management Update Component', () => {
    let comp: MisurationTypeUpdateComponent;
    let fixture: ComponentFixture<MisurationTypeUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let misurationTypeService: MisurationTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MisurationTypeUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(MisurationTypeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MisurationTypeUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      misurationTypeService = TestBed.inject(MisurationTypeService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const misurationType: IMisurationType = { id: 456 };

        activatedRoute.data = of({ misurationType });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(misurationType));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const misurationType = { id: 123 };
        spyOn(misurationTypeService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ misurationType });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: misurationType }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(misurationTypeService.update).toHaveBeenCalledWith(misurationType);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const misurationType = new MisurationType();
        spyOn(misurationTypeService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ misurationType });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: misurationType }));
        saveSubject.complete();

        // THEN
        expect(misurationTypeService.create).toHaveBeenCalledWith(misurationType);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const misurationType = { id: 123 };
        spyOn(misurationTypeService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ misurationType });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(misurationTypeService.update).toHaveBeenCalledWith(misurationType);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
