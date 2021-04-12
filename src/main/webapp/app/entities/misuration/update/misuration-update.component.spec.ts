jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MisurationService } from '../service/misuration.service';
import { IMisuration, Misuration } from '../misuration.model';
import { ICalendar } from 'app/entities/calendar/calendar.model';
import { CalendarService } from 'app/entities/calendar/service/calendar.service';
import { IMisurationType } from 'app/entities/misuration-type/misuration-type.model';
import { MisurationTypeService } from 'app/entities/misuration-type/service/misuration-type.service';

import { MisurationUpdateComponent } from './misuration-update.component';

describe('Component Tests', () => {
  describe('Misuration Management Update Component', () => {
    let comp: MisurationUpdateComponent;
    let fixture: ComponentFixture<MisurationUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let misurationService: MisurationService;
    let calendarService: CalendarService;
    let misurationTypeService: MisurationTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MisurationUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(MisurationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MisurationUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      misurationService = TestBed.inject(MisurationService);
      calendarService = TestBed.inject(CalendarService);
      misurationTypeService = TestBed.inject(MisurationTypeService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Calendar query and add missing value', () => {
        const misuration: IMisuration = { id: 456 };
        const calendar: ICalendar = { id: 82883 };
        misuration.calendar = calendar;

        const calendarCollection: ICalendar[] = [{ id: 33557 }];
        spyOn(calendarService, 'query').and.returnValue(of(new HttpResponse({ body: calendarCollection })));
        const additionalCalendars = [calendar];
        const expectedCollection: ICalendar[] = [...additionalCalendars, ...calendarCollection];
        spyOn(calendarService, 'addCalendarToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ misuration });
        comp.ngOnInit();

        expect(calendarService.query).toHaveBeenCalled();
        expect(calendarService.addCalendarToCollectionIfMissing).toHaveBeenCalledWith(calendarCollection, ...additionalCalendars);
        expect(comp.calendarsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call MisurationType query and add missing value', () => {
        const misuration: IMisuration = { id: 456 };
        const misurationType: IMisurationType = { id: 6941 };
        misuration.misurationType = misurationType;

        const misurationTypeCollection: IMisurationType[] = [{ id: 71327 }];
        spyOn(misurationTypeService, 'query').and.returnValue(of(new HttpResponse({ body: misurationTypeCollection })));
        const additionalMisurationTypes = [misurationType];
        const expectedCollection: IMisurationType[] = [...additionalMisurationTypes, ...misurationTypeCollection];
        spyOn(misurationTypeService, 'addMisurationTypeToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ misuration });
        comp.ngOnInit();

        expect(misurationTypeService.query).toHaveBeenCalled();
        expect(misurationTypeService.addMisurationTypeToCollectionIfMissing).toHaveBeenCalledWith(
          misurationTypeCollection,
          ...additionalMisurationTypes
        );
        expect(comp.misurationTypesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const misuration: IMisuration = { id: 456 };
        const calendar: ICalendar = { id: 72923 };
        misuration.calendar = calendar;
        const misurationType: IMisurationType = { id: 42162 };
        misuration.misurationType = misurationType;

        activatedRoute.data = of({ misuration });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(misuration));
        expect(comp.calendarsSharedCollection).toContain(calendar);
        expect(comp.misurationTypesSharedCollection).toContain(misurationType);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const misuration = { id: 123 };
        spyOn(misurationService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ misuration });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: misuration }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(misurationService.update).toHaveBeenCalledWith(misuration);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const misuration = new Misuration();
        spyOn(misurationService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ misuration });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: misuration }));
        saveSubject.complete();

        // THEN
        expect(misurationService.create).toHaveBeenCalledWith(misuration);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const misuration = { id: 123 };
        spyOn(misurationService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ misuration });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(misurationService.update).toHaveBeenCalledWith(misuration);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCalendarById', () => {
        it('Should return tracked Calendar primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCalendarById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackMisurationTypeById', () => {
        it('Should return tracked MisurationType primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackMisurationTypeById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
