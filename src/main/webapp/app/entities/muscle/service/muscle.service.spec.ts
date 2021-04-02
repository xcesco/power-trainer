import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMuscle, Muscle } from '../muscle.model';

import { MuscleService } from './muscle.service';

describe('Service Tests', () => {
  describe('Muscle Service', () => {
    let service: MuscleService;
    let httpMock: HttpTestingController;
    let elemDefault: IMuscle;
    let expectedResult: IMuscle | IMuscle[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(MuscleService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        uuid: 'AAAAAAA',
        name: 'AAAAAAA',
        imageContentType: 'image/png',
        image: 'AAAAAAA',
        note: 'AAAAAAA',
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

      it('should create a Muscle', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Muscle()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Muscle', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uuid: 'BBBBBB',
            name: 'BBBBBB',
            image: 'BBBBBB',
            note: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Muscle', () => {
        const patchObject = Object.assign(
          {
            uuid: 'BBBBBB',
            name: 'BBBBBB',
            image: 'BBBBBB',
            note: 'BBBBBB',
          },
          new Muscle()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Muscle', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uuid: 'BBBBBB',
            name: 'BBBBBB',
            image: 'BBBBBB',
            note: 'BBBBBB',
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

      it('should delete a Muscle', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addMuscleToCollectionIfMissing', () => {
        it('should add a Muscle to an empty array', () => {
          const muscle: IMuscle = { id: 123 };
          expectedResult = service.addMuscleToCollectionIfMissing([], muscle);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(muscle);
        });

        it('should not add a Muscle to an array that contains it', () => {
          const muscle: IMuscle = { id: 123 };
          const muscleCollection: IMuscle[] = [
            {
              ...muscle,
            },
            { id: 456 },
          ];
          expectedResult = service.addMuscleToCollectionIfMissing(muscleCollection, muscle);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Muscle to an array that doesn't contain it", () => {
          const muscle: IMuscle = { id: 123 };
          const muscleCollection: IMuscle[] = [{ id: 456 }];
          expectedResult = service.addMuscleToCollectionIfMissing(muscleCollection, muscle);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(muscle);
        });

        it('should add only unique Muscle to an array', () => {
          const muscleArray: IMuscle[] = [{ id: 123 }, { id: 456 }, { id: 13712 }];
          const muscleCollection: IMuscle[] = [{ id: 123 }];
          expectedResult = service.addMuscleToCollectionIfMissing(muscleCollection, ...muscleArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const muscle: IMuscle = { id: 123 };
          const muscle2: IMuscle = { id: 456 };
          expectedResult = service.addMuscleToCollectionIfMissing([], muscle, muscle2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(muscle);
          expect(expectedResult).toContain(muscle2);
        });

        it('should accept null and undefined values', () => {
          const muscle: IMuscle = { id: 123 };
          expectedResult = service.addMuscleToCollectionIfMissing([], null, muscle, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(muscle);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
