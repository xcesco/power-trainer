import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { WorkoutType } from 'app/entities/enumerations/workout-type.model';
import { WorkoutStatus } from 'app/entities/enumerations/workout-status.model';
import { IWorkout, Workout } from '../workout.model';

import { WorkoutService } from './workout.service';

describe('Service Tests', () => {
  describe('Workout Service', () => {
    let service: WorkoutService;
    let httpMock: HttpTestingController;
    let elemDefault: IWorkout;
    let expectedResult: IWorkout | IWorkout[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(WorkoutService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        uuid: 'AAAAAAA',
        name: 'AAAAAAA',
        imageContentType: 'image/png',
        image: 'AAAAAAA',
        type: WorkoutType.SEQUENCE,
        executionTime: 0,
        previewTime: 0,
        status: WorkoutStatus.SCHEDULED,
        date: currentDate,
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

      it('should create a Workout', () => {
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

        service.create(new Workout()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Workout', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uuid: 'BBBBBB',
            name: 'BBBBBB',
            image: 'BBBBBB',
            type: 'BBBBBB',
            executionTime: 1,
            previewTime: 1,
            status: 'BBBBBB',
            date: currentDate.format(DATE_TIME_FORMAT),
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

      it('should partial update a Workout', () => {
        const patchObject = Object.assign(
          {
            uuid: 'BBBBBB',
            previewTime: 1,
            note: 'BBBBBB',
          },
          new Workout()
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

      it('should return a list of Workout', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uuid: 'BBBBBB',
            name: 'BBBBBB',
            image: 'BBBBBB',
            type: 'BBBBBB',
            executionTime: 1,
            previewTime: 1,
            status: 'BBBBBB',
            date: currentDate.format(DATE_TIME_FORMAT),
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

      it('should delete a Workout', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addWorkoutToCollectionIfMissing', () => {
        it('should add a Workout to an empty array', () => {
          const workout: IWorkout = { id: 123 };
          expectedResult = service.addWorkoutToCollectionIfMissing([], workout);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(workout);
        });

        it('should not add a Workout to an array that contains it', () => {
          const workout: IWorkout = { id: 123 };
          const workoutCollection: IWorkout[] = [
            {
              ...workout,
            },
            { id: 456 },
          ];
          expectedResult = service.addWorkoutToCollectionIfMissing(workoutCollection, workout);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Workout to an array that doesn't contain it", () => {
          const workout: IWorkout = { id: 123 };
          const workoutCollection: IWorkout[] = [{ id: 456 }];
          expectedResult = service.addWorkoutToCollectionIfMissing(workoutCollection, workout);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(workout);
        });

        it('should add only unique Workout to an array', () => {
          const workoutArray: IWorkout[] = [{ id: 123 }, { id: 456 }, { id: 8927 }];
          const workoutCollection: IWorkout[] = [{ id: 123 }];
          expectedResult = service.addWorkoutToCollectionIfMissing(workoutCollection, ...workoutArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const workout: IWorkout = { id: 123 };
          const workout2: IWorkout = { id: 456 };
          expectedResult = service.addWorkoutToCollectionIfMissing([], workout, workout2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(workout);
          expect(expectedResult).toContain(workout2);
        });

        it('should accept null and undefined values', () => {
          const workout: IWorkout = { id: 123 };
          expectedResult = service.addWorkoutToCollectionIfMissing([], null, workout, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(workout);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
