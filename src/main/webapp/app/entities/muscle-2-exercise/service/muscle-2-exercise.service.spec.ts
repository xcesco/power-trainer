import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMuscle2Exercise, Muscle2Exercise } from '../muscle-2-exercise.model';

import { Muscle2ExerciseService } from './muscle-2-exercise.service';

describe('Service Tests', () => {
  describe('Muscle2Exercise Service', () => {
    let service: Muscle2ExerciseService;
    let httpMock: HttpTestingController;
    let elemDefault: IMuscle2Exercise;
    let expectedResult: IMuscle2Exercise | IMuscle2Exercise[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(Muscle2ExerciseService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
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

      it('should create a Muscle2Exercise', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Muscle2Exercise()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Muscle2Exercise', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Muscle2Exercise', () => {
        const patchObject = Object.assign({}, new Muscle2Exercise());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Muscle2Exercise', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
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

      it('should delete a Muscle2Exercise', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addMuscle2ExerciseToCollectionIfMissing', () => {
        it('should add a Muscle2Exercise to an empty array', () => {
          const muscle2Exercise: IMuscle2Exercise = { id: 123 };
          expectedResult = service.addMuscle2ExerciseToCollectionIfMissing([], muscle2Exercise);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(muscle2Exercise);
        });

        it('should not add a Muscle2Exercise to an array that contains it', () => {
          const muscle2Exercise: IMuscle2Exercise = { id: 123 };
          const muscle2ExerciseCollection: IMuscle2Exercise[] = [
            {
              ...muscle2Exercise,
            },
            { id: 456 },
          ];
          expectedResult = service.addMuscle2ExerciseToCollectionIfMissing(muscle2ExerciseCollection, muscle2Exercise);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Muscle2Exercise to an array that doesn't contain it", () => {
          const muscle2Exercise: IMuscle2Exercise = { id: 123 };
          const muscle2ExerciseCollection: IMuscle2Exercise[] = [{ id: 456 }];
          expectedResult = service.addMuscle2ExerciseToCollectionIfMissing(muscle2ExerciseCollection, muscle2Exercise);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(muscle2Exercise);
        });

        it('should add only unique Muscle2Exercise to an array', () => {
          const muscle2ExerciseArray: IMuscle2Exercise[] = [{ id: 123 }, { id: 456 }, { id: 23287 }];
          const muscle2ExerciseCollection: IMuscle2Exercise[] = [{ id: 123 }];
          expectedResult = service.addMuscle2ExerciseToCollectionIfMissing(muscle2ExerciseCollection, ...muscle2ExerciseArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const muscle2Exercise: IMuscle2Exercise = { id: 123 };
          const muscle2Exercise2: IMuscle2Exercise = { id: 456 };
          expectedResult = service.addMuscle2ExerciseToCollectionIfMissing([], muscle2Exercise, muscle2Exercise2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(muscle2Exercise);
          expect(expectedResult).toContain(muscle2Exercise2);
        });

        it('should accept null and undefined values', () => {
          const muscle2Exercise: IMuscle2Exercise = { id: 123 };
          expectedResult = service.addMuscle2ExerciseToCollectionIfMissing([], null, muscle2Exercise, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(muscle2Exercise);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
