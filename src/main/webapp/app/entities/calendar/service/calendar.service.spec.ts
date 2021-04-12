import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICalendar, Calendar } from '../calendar.model';

import { CalendarService } from './calendar.service';

describe('Service Tests', () => {
  describe('Calendar Service', () => {
    let service: CalendarService;
    let httpMock: HttpTestingController;
    let elemDefault: ICalendar;
    let expectedResult: ICalendar | ICalendar[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CalendarService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        uuid: 'AAAAAAA',
        name: 'AAAAAAA',
        owner: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Calendar', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Calendar()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Calendar', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uuid: 'BBBBBB',
            name: 'BBBBBB',
            owner: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Calendar', () => {
        const patchObject = Object.assign({}, new Calendar());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Calendar', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uuid: 'BBBBBB',
            name: 'BBBBBB',
            owner: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Calendar', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCalendarToCollectionIfMissing', () => {
        it('should add a Calendar to an empty array', () => {
          const calendar: ICalendar = { id: 123 };
          expectedResult = service.addCalendarToCollectionIfMissing([], calendar);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(calendar);
        });

        it('should not add a Calendar to an array that contains it', () => {
          const calendar: ICalendar = { id: 123 };
          const calendarCollection: ICalendar[] = [
            {
              ...calendar,
            },
            { id: 456 },
          ];
          expectedResult = service.addCalendarToCollectionIfMissing(calendarCollection, calendar);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Calendar to an array that doesn't contain it", () => {
          const calendar: ICalendar = { id: 123 };
          const calendarCollection: ICalendar[] = [{ id: 456 }];
          expectedResult = service.addCalendarToCollectionIfMissing(calendarCollection, calendar);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(calendar);
        });

        it('should add only unique Calendar to an array', () => {
          const calendarArray: ICalendar[] = [{ id: 123 }, { id: 456 }, { id: 99433 }];
          const calendarCollection: ICalendar[] = [{ id: 123 }];
          expectedResult = service.addCalendarToCollectionIfMissing(calendarCollection, ...calendarArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const calendar: ICalendar = { id: 123 };
          const calendar2: ICalendar = { id: 456 };
          expectedResult = service.addCalendarToCollectionIfMissing([], calendar, calendar2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(calendar);
          expect(expectedResult).toContain(calendar2);
        });

        it('should accept null and undefined values', () => {
          const calendar: ICalendar = { id: 123 };
          expectedResult = service.addCalendarToCollectionIfMissing([], null, calendar, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(calendar);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
