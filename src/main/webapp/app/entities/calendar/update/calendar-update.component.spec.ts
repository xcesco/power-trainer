jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CalendarService } from '../service/calendar.service';
import { ICalendar, Calendar } from '../calendar.model';

import { CalendarUpdateComponent } from './calendar-update.component';

describe('Component Tests', () => {
  describe('Calendar Management Update Component', () => {
    let comp: CalendarUpdateComponent;
    let fixture: ComponentFixture<CalendarUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let calendarService: CalendarService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CalendarUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CalendarUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CalendarUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      calendarService = TestBed.inject(CalendarService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const calendar: ICalendar = { id: 456 };

        activatedRoute.data = of({ calendar });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(calendar));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const calendar = { id: 123 };
        spyOn(calendarService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ calendar });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: calendar }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(calendarService.update).toHaveBeenCalledWith(calendar);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const calendar = new Calendar();
        spyOn(calendarService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ calendar });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: calendar }));
        saveSubject.complete();

        // THEN
        expect(calendarService.create).toHaveBeenCalledWith(calendar);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const calendar = { id: 123 };
        spyOn(calendarService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ calendar });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(calendarService.update).toHaveBeenCalledWith(calendar);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
