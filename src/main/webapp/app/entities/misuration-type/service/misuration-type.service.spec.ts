import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMisurationType, MisurationType } from '../misuration-type.model';

import { MisurationTypeService } from './misuration-type.service';

describe('Service Tests', () => {
  describe('MisurationType Service', () => {
    let service: MisurationTypeService;
    let httpMock: HttpTestingController;
    let elemDefault: IMisurationType;
    let expectedResult: IMisurationType | IMisurationType[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(MisurationTypeService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        uuid: 'AAAAAAA',
        name: 'AAAAAAA',
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

      it('should create a MisurationType', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new MisurationType()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a MisurationType', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uuid: 'BBBBBB',
            name: 'BBBBBB',
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

      it('should partial update a MisurationType', () => {
        const patchObject = Object.assign(
          {
            uuid: 'BBBBBB',
            name: 'BBBBBB',
            description: 'BBBBBB',
          },
          new MisurationType()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of MisurationType', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uuid: 'BBBBBB',
            name: 'BBBBBB',
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

      it('should delete a MisurationType', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addMisurationTypeToCollectionIfMissing', () => {
        it('should add a MisurationType to an empty array', () => {
          const misurationType: IMisurationType = { id: 123 };
          expectedResult = service.addMisurationTypeToCollectionIfMissing([], misurationType);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(misurationType);
        });

        it('should not add a MisurationType to an array that contains it', () => {
          const misurationType: IMisurationType = { id: 123 };
          const misurationTypeCollection: IMisurationType[] = [
            {
              ...misurationType,
            },
            { id: 456 },
          ];
          expectedResult = service.addMisurationTypeToCollectionIfMissing(misurationTypeCollection, misurationType);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a MisurationType to an array that doesn't contain it", () => {
          const misurationType: IMisurationType = { id: 123 };
          const misurationTypeCollection: IMisurationType[] = [{ id: 456 }];
          expectedResult = service.addMisurationTypeToCollectionIfMissing(misurationTypeCollection, misurationType);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(misurationType);
        });

        it('should add only unique MisurationType to an array', () => {
          const misurationTypeArray: IMisurationType[] = [{ id: 123 }, { id: 456 }, { id: 16682 }];
          const misurationTypeCollection: IMisurationType[] = [{ id: 123 }];
          expectedResult = service.addMisurationTypeToCollectionIfMissing(misurationTypeCollection, ...misurationTypeArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const misurationType: IMisurationType = { id: 123 };
          const misurationType2: IMisurationType = { id: 456 };
          expectedResult = service.addMisurationTypeToCollectionIfMissing([], misurationType, misurationType2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(misurationType);
          expect(expectedResult).toContain(misurationType2);
        });

        it('should accept null and undefined values', () => {
          const misurationType: IMisurationType = { id: 123 };
          expectedResult = service.addMisurationTypeToCollectionIfMissing([], null, misurationType, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(misurationType);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
