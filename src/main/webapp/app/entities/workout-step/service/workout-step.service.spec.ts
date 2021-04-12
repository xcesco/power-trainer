import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { WorkoutStepType } from 'app/entities/enumerations/workout-step-type.model';
import { WorkoutStatus } from 'app/entities/enumerations/workout-status.model';
import { ValueType } from 'app/entities/enumerations/value-type.model';
import { IWorkoutStep, WorkoutStep } from '../workout-step.model';

import { WorkoutStepService } from './workout-step.service';

describe('Service Tests', () => {
  describe('WorkoutStep Service', () => {
    let service: WorkoutStepService;
    let httpMock: HttpTestingController;
    let elemDefault: IWorkoutStep;
    let expectedResult: IWorkoutStep | IWorkoutStep[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(WorkoutStepService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        uuid: 'AAAAAAA',
        order: 0,
        executionTime: 0,
        type: WorkoutStepType.PREPARE_TIME,
        status: WorkoutStatus.SCHEDULED,
        exerciseUuid: 'AAAAAAA',
        exerciseName: 'AAAAAAA',
        exerciseValue: 0,
        exerciseValueType: ValueType.DURATION,
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

      it('should create a WorkoutStep', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new WorkoutStep()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a WorkoutStep', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uuid: 'BBBBBB',
            order: 1,
            executionTime: 1,
            type: 'BBBBBB',
            status: 'BBBBBB',
            exerciseUuid: 'BBBBBB',
            exerciseName: 'BBBBBB',
            exerciseValue: 1,
            exerciseValueType: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a WorkoutStep', () => {
        const patchObject = Object.assign(
          {
            type: 'BBBBBB',
            status: 'BBBBBB',
            exerciseUuid: 'BBBBBB',
          },
          new WorkoutStep()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of WorkoutStep', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uuid: 'BBBBBB',
            order: 1,
            executionTime: 1,
            type: 'BBBBBB',
            status: 'BBBBBB',
            exerciseUuid: 'BBBBBB',
            exerciseName: 'BBBBBB',
            exerciseValue: 1,
            exerciseValueType: 'BBBBBB',
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

      it('should delete a WorkoutStep', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addWorkoutStepToCollectionIfMissing', () => {
        it('should add a WorkoutStep to an empty array', () => {
          const workoutStep: IWorkoutStep = { id: 123 };
          expectedResult = service.addWorkoutStepToCollectionIfMissing([], workoutStep);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(workoutStep);
        });

        it('should not add a WorkoutStep to an array that contains it', () => {
          const workoutStep: IWorkoutStep = { id: 123 };
          const workoutStepCollection: IWorkoutStep[] = [
            {
              ...workoutStep,
            },
            { id: 456 },
          ];
          expectedResult = service.addWorkoutStepToCollectionIfMissing(workoutStepCollection, workoutStep);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a WorkoutStep to an array that doesn't contain it", () => {
          const workoutStep: IWorkoutStep = { id: 123 };
          const workoutStepCollection: IWorkoutStep[] = [{ id: 456 }];
          expectedResult = service.addWorkoutStepToCollectionIfMissing(workoutStepCollection, workoutStep);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(workoutStep);
        });

        it('should add only unique WorkoutStep to an array', () => {
          const workoutStepArray: IWorkoutStep[] = [{ id: 123 }, { id: 456 }, { id: 24587 }];
          const workoutStepCollection: IWorkoutStep[] = [{ id: 123 }];
          expectedResult = service.addWorkoutStepToCollectionIfMissing(workoutStepCollection, ...workoutStepArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const workoutStep: IWorkoutStep = { id: 123 };
          const workoutStep2: IWorkoutStep = { id: 456 };
          expectedResult = service.addWorkoutStepToCollectionIfMissing([], workoutStep, workoutStep2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(workoutStep);
          expect(expectedResult).toContain(workoutStep2);
        });

        it('should accept null and undefined values', () => {
          const workoutStep: IWorkoutStep = { id: 123 };
          expectedResult = service.addWorkoutStepToCollectionIfMissing([], null, workoutStep, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(workoutStep);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
