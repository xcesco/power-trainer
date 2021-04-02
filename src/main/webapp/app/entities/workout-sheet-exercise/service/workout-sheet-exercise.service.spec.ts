import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ValueType } from 'app/entities/enumerations/value-type.model';
import { IWorkoutSheetExercise, WorkoutSheetExercise } from '../workout-sheet-exercise.model';

import { WorkoutSheetExerciseService } from './workout-sheet-exercise.service';

describe('Service Tests', () => {
  describe('WorkoutSheetExercise Service', () => {
    let service: WorkoutSheetExerciseService;
    let httpMock: HttpTestingController;
    let elemDefault: IWorkoutSheetExercise;
    let expectedResult: IWorkoutSheetExercise | IWorkoutSheetExercise[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(WorkoutSheetExerciseService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        uuid: 'AAAAAAA',
        order: 0,
        repetition: 0,
        value: 0,
        valueType: ValueType.DURATION,
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

      it('should create a WorkoutSheetExercise', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new WorkoutSheetExercise()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a WorkoutSheetExercise', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uuid: 'BBBBBB',
            order: 1,
            repetition: 1,
            value: 1,
            valueType: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a WorkoutSheetExercise', () => {
        const patchObject = Object.assign(
          {
            uuid: 'BBBBBB',
            order: 1,
            repetition: 1,
            valueType: 'BBBBBB',
          },
          new WorkoutSheetExercise()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of WorkoutSheetExercise', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uuid: 'BBBBBB',
            order: 1,
            repetition: 1,
            value: 1,
            valueType: 'BBBBBB',
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

      it('should delete a WorkoutSheetExercise', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addWorkoutSheetExerciseToCollectionIfMissing', () => {
        it('should add a WorkoutSheetExercise to an empty array', () => {
          const workoutSheetExercise: IWorkoutSheetExercise = { id: 123 };
          expectedResult = service.addWorkoutSheetExerciseToCollectionIfMissing([], workoutSheetExercise);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(workoutSheetExercise);
        });

        it('should not add a WorkoutSheetExercise to an array that contains it', () => {
          const workoutSheetExercise: IWorkoutSheetExercise = { id: 123 };
          const workoutSheetExerciseCollection: IWorkoutSheetExercise[] = [
            {
              ...workoutSheetExercise,
            },
            { id: 456 },
          ];
          expectedResult = service.addWorkoutSheetExerciseToCollectionIfMissing(workoutSheetExerciseCollection, workoutSheetExercise);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a WorkoutSheetExercise to an array that doesn't contain it", () => {
          const workoutSheetExercise: IWorkoutSheetExercise = { id: 123 };
          const workoutSheetExerciseCollection: IWorkoutSheetExercise[] = [{ id: 456 }];
          expectedResult = service.addWorkoutSheetExerciseToCollectionIfMissing(workoutSheetExerciseCollection, workoutSheetExercise);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(workoutSheetExercise);
        });

        it('should add only unique WorkoutSheetExercise to an array', () => {
          const workoutSheetExerciseArray: IWorkoutSheetExercise[] = [{ id: 123 }, { id: 456 }, { id: 96293 }];
          const workoutSheetExerciseCollection: IWorkoutSheetExercise[] = [{ id: 123 }];
          expectedResult = service.addWorkoutSheetExerciseToCollectionIfMissing(
            workoutSheetExerciseCollection,
            ...workoutSheetExerciseArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const workoutSheetExercise: IWorkoutSheetExercise = { id: 123 };
          const workoutSheetExercise2: IWorkoutSheetExercise = { id: 456 };
          expectedResult = service.addWorkoutSheetExerciseToCollectionIfMissing([], workoutSheetExercise, workoutSheetExercise2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(workoutSheetExercise);
          expect(expectedResult).toContain(workoutSheetExercise2);
        });

        it('should accept null and undefined values', () => {
          const workoutSheetExercise: IWorkoutSheetExercise = { id: 123 };
          expectedResult = service.addWorkoutSheetExerciseToCollectionIfMissing([], null, workoutSheetExercise, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(workoutSheetExercise);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
