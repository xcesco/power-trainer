import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMeasureType, MeasureType } from '../measure-type.model';

import { MeasureTypeService } from './measure-type.service';

describe('Service Tests', () => {
  describe('MeasureType Service', () => {
    let service: MeasureTypeService;
    let httpMock: HttpTestingController;
    let elemDefault: IMeasureType;
    let expectedResult: IMeasureType | IMeasureType[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(MeasureTypeService);
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

      it('should create a MeasureType', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new MeasureType()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a MeasureType', () => {
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

      it('should partial update a MeasureType', () => {
        const patchObject = Object.assign(
          {
            uuid: 'BBBBBB',
            name: 'BBBBBB',
            image: 'BBBBBB',
            note: 'BBBBBB',
          },
          new MeasureType()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of MeasureType', () => {
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

      it('should delete a MeasureType', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addMeasureTypeToCollectionIfMissing', () => {
        it('should add a MeasureType to an empty array', () => {
          const measureType: IMeasureType = { id: 123 };
          expectedResult = service.addMeasureTypeToCollectionIfMissing([], measureType);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(measureType);
        });

        it('should not add a MeasureType to an array that contains it', () => {
          const measureType: IMeasureType = { id: 123 };
          const measureTypeCollection: IMeasureType[] = [
            {
              ...measureType,
            },
            { id: 456 },
          ];
          expectedResult = service.addMeasureTypeToCollectionIfMissing(measureTypeCollection, measureType);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a MeasureType to an array that doesn't contain it", () => {
          const measureType: IMeasureType = { id: 123 };
          const measureTypeCollection: IMeasureType[] = [{ id: 456 }];
          expectedResult = service.addMeasureTypeToCollectionIfMissing(measureTypeCollection, measureType);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(measureType);
        });

        it('should add only unique MeasureType to an array', () => {
          const measureTypeArray: IMeasureType[] = [{ id: 123 }, { id: 456 }, { id: 73594 }];
          const measureTypeCollection: IMeasureType[] = [{ id: 123 }];
          expectedResult = service.addMeasureTypeToCollectionIfMissing(measureTypeCollection, ...measureTypeArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const measureType: IMeasureType = { id: 123 };
          const measureType2: IMeasureType = { id: 456 };
          expectedResult = service.addMeasureTypeToCollectionIfMissing([], measureType, measureType2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(measureType);
          expect(expectedResult).toContain(measureType2);
        });

        it('should accept null and undefined values', () => {
          const measureType: IMeasureType = { id: 123 };
          expectedResult = service.addMeasureTypeToCollectionIfMissing([], null, measureType, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(measureType);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
