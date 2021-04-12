import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IExerciseTool, ExerciseTool } from '../exercise-tool.model';

import { ExerciseToolService } from './exercise-tool.service';

describe('Service Tests', () => {
  describe('ExerciseTool Service', () => {
    let service: ExerciseToolService;
    let httpMock: HttpTestingController;
    let elemDefault: IExerciseTool;
    let expectedResult: IExerciseTool | IExerciseTool[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ExerciseToolService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        uuid: 'AAAAAAA',
        imageContentType: 'image/png',
        image: 'AAAAAAA',
        name: 'AAAAAAA',
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

      it('should create a ExerciseTool', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ExerciseTool()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ExerciseTool', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uuid: 'BBBBBB',
            image: 'BBBBBB',
            name: 'BBBBBB',
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

      it('should partial update a ExerciseTool', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
          },
          new ExerciseTool()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ExerciseTool', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uuid: 'BBBBBB',
            image: 'BBBBBB',
            name: 'BBBBBB',
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

      it('should delete a ExerciseTool', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addExerciseToolToCollectionIfMissing', () => {
        it('should add a ExerciseTool to an empty array', () => {
          const exerciseTool: IExerciseTool = { id: 123 };
          expectedResult = service.addExerciseToolToCollectionIfMissing([], exerciseTool);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(exerciseTool);
        });

        it('should not add a ExerciseTool to an array that contains it', () => {
          const exerciseTool: IExerciseTool = { id: 123 };
          const exerciseToolCollection: IExerciseTool[] = [
            {
              ...exerciseTool,
            },
            { id: 456 },
          ];
          expectedResult = service.addExerciseToolToCollectionIfMissing(exerciseToolCollection, exerciseTool);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ExerciseTool to an array that doesn't contain it", () => {
          const exerciseTool: IExerciseTool = { id: 123 };
          const exerciseToolCollection: IExerciseTool[] = [{ id: 456 }];
          expectedResult = service.addExerciseToolToCollectionIfMissing(exerciseToolCollection, exerciseTool);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(exerciseTool);
        });

        it('should add only unique ExerciseTool to an array', () => {
          const exerciseToolArray: IExerciseTool[] = [{ id: 123 }, { id: 456 }, { id: 24383 }];
          const exerciseToolCollection: IExerciseTool[] = [{ id: 123 }];
          expectedResult = service.addExerciseToolToCollectionIfMissing(exerciseToolCollection, ...exerciseToolArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const exerciseTool: IExerciseTool = { id: 123 };
          const exerciseTool2: IExerciseTool = { id: 456 };
          expectedResult = service.addExerciseToolToCollectionIfMissing([], exerciseTool, exerciseTool2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(exerciseTool);
          expect(expectedResult).toContain(exerciseTool2);
        });

        it('should accept null and undefined values', () => {
          const exerciseTool: IExerciseTool = { id: 123 };
          expectedResult = service.addExerciseToolToCollectionIfMissing([], null, exerciseTool, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(exerciseTool);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
