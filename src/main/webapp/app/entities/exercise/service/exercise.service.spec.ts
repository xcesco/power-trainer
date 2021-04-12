import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ValueType } from 'app/entities/enumerations/value-type.model';
import { IExercise, Exercise } from '../exercise.model';

import { ExerciseService } from './exercise.service';

describe('Service Tests', () => {
  describe('Exercise Service', () => {
    let service: ExerciseService;
    let httpMock: HttpTestingController;
    let elemDefault: IExercise;
    let expectedResult: IExercise | IExercise[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ExerciseService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        uuid: 'AAAAAAA',
        imageContentType: 'image/png',
        image: 'AAAAAAA',
        name: 'AAAAAAA',
        description: 'AAAAAAA',
        valueType: ValueType.DURATION,
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

      it('should create a Exercise', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Exercise()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Exercise', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uuid: 'BBBBBB',
            image: 'BBBBBB',
            name: 'BBBBBB',
            description: 'BBBBBB',
            valueType: 'BBBBBB',
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

      it('should partial update a Exercise', () => {
        const patchObject = Object.assign(
          {
            uuid: 'BBBBBB',
            image: 'BBBBBB',
            name: 'BBBBBB',
            description: 'BBBBBB',
            valueType: 'BBBBBB',
            owner: 'BBBBBB',
          },
          new Exercise()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Exercise', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uuid: 'BBBBBB',
            image: 'BBBBBB',
            name: 'BBBBBB',
            description: 'BBBBBB',
            valueType: 'BBBBBB',
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

      it('should delete a Exercise', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addExerciseToCollectionIfMissing', () => {
        it('should add a Exercise to an empty array', () => {
          const exercise: IExercise = { id: 123 };
          expectedResult = service.addExerciseToCollectionIfMissing([], exercise);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(exercise);
        });

        it('should not add a Exercise to an array that contains it', () => {
          const exercise: IExercise = { id: 123 };
          const exerciseCollection: IExercise[] = [
            {
              ...exercise,
            },
            { id: 456 },
          ];
          expectedResult = service.addExerciseToCollectionIfMissing(exerciseCollection, exercise);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Exercise to an array that doesn't contain it", () => {
          const exercise: IExercise = { id: 123 };
          const exerciseCollection: IExercise[] = [{ id: 456 }];
          expectedResult = service.addExerciseToCollectionIfMissing(exerciseCollection, exercise);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(exercise);
        });

        it('should add only unique Exercise to an array', () => {
          const exerciseArray: IExercise[] = [{ id: 123 }, { id: 456 }, { id: 99516 }];
          const exerciseCollection: IExercise[] = [{ id: 123 }];
          expectedResult = service.addExerciseToCollectionIfMissing(exerciseCollection, ...exerciseArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const exercise: IExercise = { id: 123 };
          const exercise2: IExercise = { id: 456 };
          expectedResult = service.addExerciseToCollectionIfMissing([], exercise, exercise2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(exercise);
          expect(expectedResult).toContain(exercise2);
        });

        it('should accept null and undefined values', () => {
          const exercise: IExercise = { id: 123 };
          expectedResult = service.addExerciseToCollectionIfMissing([], null, exercise, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(exercise);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
