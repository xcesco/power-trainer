jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ExerciseValueService } from '../service/exercise-value.service';
import { IExerciseValue, ExerciseValue } from '../exercise-value.model';
import { ICalendar } from 'app/entities/calendar/calendar.model';
import { CalendarService } from 'app/entities/calendar/service/calendar.service';

import { ExerciseValueUpdateComponent } from './exercise-value-update.component';

describe('Component Tests', () => {
  describe('ExerciseValue Management Update Component', () => {
    let comp: ExerciseValueUpdateComponent;
    let fixture: ComponentFixture<ExerciseValueUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let exerciseValueService: ExerciseValueService;
    let calendarService: CalendarService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ExerciseValueUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ExerciseValueUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ExerciseValueUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      exerciseValueService = TestBed.inject(ExerciseValueService);
      calendarService = TestBed.inject(CalendarService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Calendar query and add missing value', () => {
        const exerciseValue: IExerciseValue = { id: 456 };
        const calendar: ICalendar = { id: 87540 };
        exerciseValue.calendar = calendar;

        const calendarCollection: ICalendar[] = [{ id: 10396 }];
        spyOn(calendarService, 'query').and.returnValue(of(new HttpResponse({ body: calendarCollection })));
        const additionalCalendars = [calendar];
        const expectedCollection: ICalendar[] = [...additionalCalendars, ...calendarCollection];
        spyOn(calendarService, 'addCalendarToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ exerciseValue });
        comp.ngOnInit();

        expect(calendarService.query).toHaveBeenCalled();
        expect(calendarService.addCalendarToCollectionIfMissing).toHaveBeenCalledWith(calendarCollection, ...additionalCalendars);
        expect(comp.calendarsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const exerciseValue: IExerciseValue = { id: 456 };
        const calendar: ICalendar = { id: 28846 };
        exerciseValue.calendar = calendar;

        activatedRoute.data = of({ exerciseValue });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(exerciseValue));
        expect(comp.calendarsSharedCollection).toContain(calendar);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const exerciseValue = { id: 123 };
        spyOn(exerciseValueService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ exerciseValue });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: exerciseValue }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(exerciseValueService.update).toHaveBeenCalledWith(exerciseValue);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const exerciseValue = new ExerciseValue();
        spyOn(exerciseValueService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ exerciseValue });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: exerciseValue }));
        saveSubject.complete();

        // THEN
        expect(exerciseValueService.create).toHaveBeenCalledWith(exerciseValue);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const exerciseValue = { id: 123 };
        spyOn(exerciseValueService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ exerciseValue });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(exerciseValueService.update).toHaveBeenCalledWith(exerciseValue);
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
    });
  });
});
