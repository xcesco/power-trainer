import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { WorkoutType } from 'app/entities/enumerations/workout-type.model';
import { IWorkoutSheet, WorkoutSheet } from '../workout-sheet.model';

import { WorkoutSheetService } from './workout-sheet.service';

describe('Service Tests', () => {
  describe('WorkoutSheet Service', () => {
    let service: WorkoutSheetService;
    let httpMock: HttpTestingController;
    let elemDefault: IWorkoutSheet;
    let expectedResult: IWorkoutSheet | IWorkoutSheet[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(WorkoutSheetService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        uuid: 'AAAAAAA',
        name: 'AAAAAAA',
        imageContentType: 'image/png',
        image: 'AAAAAAA',
        description: 'AAAAAAA',
        prepareTime: 0,
        coolDownTime: 0,
        cycles: 0,
        cycleRestTime: 0,
        set: 0,
        setRestTime: 0,
        type: WorkoutType.SEQUENCE,
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

      it('should create a WorkoutSheet', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new WorkoutSheet()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a WorkoutSheet', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uuid: 'BBBBBB',
            name: 'BBBBBB',
            image: 'BBBBBB',
            description: 'BBBBBB',
            prepareTime: 1,
            coolDownTime: 1,
            cycles: 1,
            cycleRestTime: 1,
            set: 1,
            setRestTime: 1,
            type: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a WorkoutSheet', () => {
        const patchObject = Object.assign(
          {
            uuid: 'BBBBBB',
            name: 'BBBBBB',
            description: 'BBBBBB',
            prepareTime: 1,
            set: 1,
            setRestTime: 1,
            type: 'BBBBBB',
          },
          new WorkoutSheet()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of WorkoutSheet', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uuid: 'BBBBBB',
            name: 'BBBBBB',
            image: 'BBBBBB',
            description: 'BBBBBB',
            prepareTime: 1,
            coolDownTime: 1,
            cycles: 1,
            cycleRestTime: 1,
            set: 1,
            setRestTime: 1,
            type: 'BBBBBB',
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

      it('should delete a WorkoutSheet', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addWorkoutSheetToCollectionIfMissing', () => {
        it('should add a WorkoutSheet to an empty array', () => {
          const workoutSheet: IWorkoutSheet = { id: 123 };
          expectedResult = service.addWorkoutSheetToCollectionIfMissing([], workoutSheet);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(workoutSheet);
        });

        it('should not add a WorkoutSheet to an array that contains it', () => {
          const workoutSheet: IWorkoutSheet = { id: 123 };
          const workoutSheetCollection: IWorkoutSheet[] = [
            {
              ...workoutSheet,
            },
            { id: 456 },
          ];
          expectedResult = service.addWorkoutSheetToCollectionIfMissing(workoutSheetCollection, workoutSheet);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a WorkoutSheet to an array that doesn't contain it", () => {
          const workoutSheet: IWorkoutSheet = { id: 123 };
          const workoutSheetCollection: IWorkoutSheet[] = [{ id: 456 }];
          expectedResult = service.addWorkoutSheetToCollectionIfMissing(workoutSheetCollection, workoutSheet);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(workoutSheet);
        });

        it('should add only unique WorkoutSheet to an array', () => {
          const workoutSheetArray: IWorkoutSheet[] = [{ id: 123 }, { id: 456 }, { id: 19575 }];
          const workoutSheetCollection: IWorkoutSheet[] = [{ id: 123 }];
          expectedResult = service.addWorkoutSheetToCollectionIfMissing(workoutSheetCollection, ...workoutSheetArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const workoutSheet: IWorkoutSheet = { id: 123 };
          const workoutSheet2: IWorkoutSheet = { id: 456 };
          expectedResult = service.addWorkoutSheetToCollectionIfMissing([], workoutSheet, workoutSheet2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(workoutSheet);
          expect(expectedResult).toContain(workoutSheet2);
        });

        it('should accept null and undefined values', () => {
          const workoutSheet: IWorkoutSheet = { id: 123 };
          expectedResult = service.addWorkoutSheetToCollectionIfMissing([], null, workoutSheet, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(workoutSheet);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
