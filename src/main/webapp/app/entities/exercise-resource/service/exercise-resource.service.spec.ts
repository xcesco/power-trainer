import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ExerciseResourceType } from 'app/entities/enumerations/exercise-resource-type.model';
import { IExerciseResource, ExerciseResource } from '../exercise-resource.model';

import { ExerciseResourceService } from './exercise-resource.service';

describe('Service Tests', () => {
  describe('ExerciseResource Service', () => {
    let service: ExerciseResourceService;
    let httpMock: HttpTestingController;
    let elemDefault: IExerciseResource;
    let expectedResult: IExerciseResource | IExerciseResource[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ExerciseResourceService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        uuid: 'AAAAAAA',
        order: 0,
        type: ExerciseResourceType.VIDEO,
        url: 'AAAAAAA',
        imageContentType: 'image/png',
        image: 'AAAAAAA',
        description: 'AAAAAAA',
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

      it('should create a ExerciseResource', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ExerciseResource()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ExerciseResource', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uuid: 'BBBBBB',
            order: 1,
            type: 'BBBBBB',
            url: 'BBBBBB',
            image: 'BBBBBB',
            description: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ExerciseResource', () => {
        const patchObject = Object.assign(
          {
            type: 'BBBBBB',
            url: 'BBBBBB',
            image: 'BBBBBB',
            description: 'BBBBBB',
          },
          new ExerciseResource()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ExerciseResource', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uuid: 'BBBBBB',
            order: 1,
            type: 'BBBBBB',
            url: 'BBBBBB',
            image: 'BBBBBB',
            description: 'BBBBBB',
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

      it('should delete a ExerciseResource', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addExerciseResourceToCollectionIfMissing', () => {
        it('should add a ExerciseResource to an empty array', () => {
          const exerciseResource: IExerciseResource = { id: 123 };
          expectedResult = service.addExerciseResourceToCollectionIfMissing([], exerciseResource);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(exerciseResource);
        });

        it('should not add a ExerciseResource to an array that contains it', () => {
          const exerciseResource: IExerciseResource = { id: 123 };
          const exerciseResourceCollection: IExerciseResource[] = [
            {
              ...exerciseResource,
            },
            { id: 456 },
          ];
          expectedResult = service.addExerciseResourceToCollectionIfMissing(exerciseResourceCollection, exerciseResource);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ExerciseResource to an array that doesn't contain it", () => {
          const exerciseResource: IExerciseResource = { id: 123 };
          const exerciseResourceCollection: IExerciseResource[] = [{ id: 456 }];
          expectedResult = service.addExerciseResourceToCollectionIfMissing(exerciseResourceCollection, exerciseResource);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(exerciseResource);
        });

        it('should add only unique ExerciseResource to an array', () => {
          const exerciseResourceArray: IExerciseResource[] = [{ id: 123 }, { id: 456 }, { id: 28679 }];
          const exerciseResourceCollection: IExerciseResource[] = [{ id: 123 }];
          expectedResult = service.addExerciseResourceToCollectionIfMissing(exerciseResourceCollection, ...exerciseResourceArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const exerciseResource: IExerciseResource = { id: 123 };
          const exerciseResource2: IExerciseResource = { id: 456 };
          expectedResult = service.addExerciseResourceToCollectionIfMissing([], exerciseResource, exerciseResource2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(exerciseResource);
          expect(expectedResult).toContain(exerciseResource2);
        });

        it('should accept null and undefined values', () => {
          const exerciseResource: IExerciseResource = { id: 123 };
          expectedResult = service.addExerciseResourceToCollectionIfMissing([], null, exerciseResource, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(exerciseResource);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
