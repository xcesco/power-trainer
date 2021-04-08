import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ValueType } from 'app/entities/enumerations/value-type.model';
import { IExerciseValue, ExerciseValue } from '../exercise-value.model';

import { ExerciseValueService } from './exercise-value.service';

describe('Service Tests', () => {
  describe('ExerciseValue Service', () => {
    let service: ExerciseValueService;
    let httpMock: HttpTestingController;
    let elemDefault: IExerciseValue;
    let expectedResult: IExerciseValue | IExerciseValue[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ExerciseValueService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        uuid: 'AAAAAAA',
        date: currentDate,
        exerciseUuid: 'AAAAAAA',
        exerciseName: 'AAAAAAA',
        exerciseValue: 0,
        exerciseValueType: ValueType.DURATION,
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

      it('should create a ExerciseValue', () => {
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

        service.create(new ExerciseValue()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ExerciseValue', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uuid: 'BBBBBB',
            date: currentDate.format(DATE_TIME_FORMAT),
            exerciseUuid: 'BBBBBB',
            exerciseName: 'BBBBBB',
            exerciseValue: 1,
            exerciseValueType: 'BBBBBB',
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

      it('should partial update a ExerciseValue', () => {
        const patchObject = Object.assign(
          {
            uuid: 'BBBBBB',
            date: currentDate.format(DATE_TIME_FORMAT),
            exerciseUuid: 'BBBBBB',
          },
          new ExerciseValue()
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

      it('should return a list of ExerciseValue', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uuid: 'BBBBBB',
            date: currentDate.format(DATE_TIME_FORMAT),
            exerciseUuid: 'BBBBBB',
            exerciseName: 'BBBBBB',
            exerciseValue: 1,
            exerciseValueType: 'BBBBBB',
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

      it('should delete a ExerciseValue', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addExerciseValueToCollectionIfMissing', () => {
        it('should add a ExerciseValue to an empty array', () => {
          const exerciseValue: IExerciseValue = { id: 123 };
          expectedResult = service.addExerciseValueToCollectionIfMissing([], exerciseValue);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(exerciseValue);
        });

        it('should not add a ExerciseValue to an array that contains it', () => {
          const exerciseValue: IExerciseValue = { id: 123 };
          const exerciseValueCollection: IExerciseValue[] = [
            {
              ...exerciseValue,
            },
            { id: 456 },
          ];
          expectedResult = service.addExerciseValueToCollectionIfMissing(exerciseValueCollection, exerciseValue);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ExerciseValue to an array that doesn't contain it", () => {
          const exerciseValue: IExerciseValue = { id: 123 };
          const exerciseValueCollection: IExerciseValue[] = [{ id: 456 }];
          expectedResult = service.addExerciseValueToCollectionIfMissing(exerciseValueCollection, exerciseValue);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(exerciseValue);
        });

        it('should add only unique ExerciseValue to an array', () => {
          const exerciseValueArray: IExerciseValue[] = [{ id: 123 }, { id: 456 }, { id: 56221 }];
          const exerciseValueCollection: IExerciseValue[] = [{ id: 123 }];
          expectedResult = service.addExerciseValueToCollectionIfMissing(exerciseValueCollection, ...exerciseValueArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const exerciseValue: IExerciseValue = { id: 123 };
          const exerciseValue2: IExerciseValue = { id: 456 };
          expectedResult = service.addExerciseValueToCollectionIfMissing([], exerciseValue, exerciseValue2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(exerciseValue);
          expect(expectedResult).toContain(exerciseValue2);
        });

        it('should accept null and undefined values', () => {
          const exerciseValue: IExerciseValue = { id: 123 };
          expectedResult = service.addExerciseValueToCollectionIfMissing([], null, exerciseValue, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(exerciseValue);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
