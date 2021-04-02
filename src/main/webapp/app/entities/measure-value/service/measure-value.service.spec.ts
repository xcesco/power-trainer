import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMeasureValue, MeasureValue } from '../measure-value.model';

import { MeasureValueService } from './measure-value.service';

describe('Service Tests', () => {
  describe('MeasureValue Service', () => {
    let service: MeasureValueService;
    let httpMock: HttpTestingController;
    let elemDefault: IMeasureValue;
    let expectedResult: IMeasureValue | IMeasureValue[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(MeasureValueService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        uuid: 'AAAAAAA',
        date: currentDate,
        value: 0,
        note: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            date: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a MeasureValue', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            date: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.create(new MeasureValue()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a MeasureValue', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uuid: 'BBBBBB',
            date: currentDate.format(DATE_TIME_FORMAT),
            value: 1,
            note: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a MeasureValue', () => {
        const patchObject = Object.assign(
          {
            uuid: 'BBBBBB',
            value: 1,
            note: 'BBBBBB',
          },
          new MeasureValue()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of MeasureValue', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uuid: 'BBBBBB',
            date: currentDate.format(DATE_TIME_FORMAT),
            value: 1,
            note: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a MeasureValue', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addMeasureValueToCollectionIfMissing', () => {
        it('should add a MeasureValue to an empty array', () => {
          const measureValue: IMeasureValue = { id: 123 };
          expectedResult = service.addMeasureValueToCollectionIfMissing([], measureValue);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(measureValue);
        });

        it('should not add a MeasureValue to an array that contains it', () => {
          const measureValue: IMeasureValue = { id: 123 };
          const measureValueCollection: IMeasureValue[] = [
            {
              ...measureValue,
            },
            { id: 456 },
          ];
          expectedResult = service.addMeasureValueToCollectionIfMissing(measureValueCollection, measureValue);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a MeasureValue to an array that doesn't contain it", () => {
          const measureValue: IMeasureValue = { id: 123 };
          const measureValueCollection: IMeasureValue[] = [{ id: 456 }];
          expectedResult = service.addMeasureValueToCollectionIfMissing(measureValueCollection, measureValue);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(measureValue);
        });

        it('should add only unique MeasureValue to an array', () => {
          const measureValueArray: IMeasureValue[] = [{ id: 123 }, { id: 456 }, { id: 90673 }];
          const measureValueCollection: IMeasureValue[] = [{ id: 123 }];
          expectedResult = service.addMeasureValueToCollectionIfMissing(measureValueCollection, ...measureValueArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const measureValue: IMeasureValue = { id: 123 };
          const measureValue2: IMeasureValue = { id: 456 };
          expectedResult = service.addMeasureValueToCollectionIfMissing([], measureValue, measureValue2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(measureValue);
          expect(expectedResult).toContain(measureValue2);
        });

        it('should accept null and undefined values', () => {
          const measureValue: IMeasureValue = { id: 123 };
          expectedResult = service.addMeasureValueToCollectionIfMissing([], null, measureValue, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(measureValue);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
