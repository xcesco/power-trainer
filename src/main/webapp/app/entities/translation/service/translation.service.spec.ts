import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITranslation, Translation } from '../translation.model';

import { TranslationService } from './translation.service';

describe('Service Tests', () => {
  describe('Translation Service', () => {
    let service: TranslationService;
    let httpMock: HttpTestingController;
    let elemDefault: ITranslation;
    let expectedResult: ITranslation | ITranslation[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(TranslationService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        entityType: 'AAAAAAA',
        entityUuid: 'AAAAAAA',
        value: 'AAAAAAA',
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

      it('should create a Translation', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Translation()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Translation', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            entityType: 'BBBBBB',
            entityUuid: 'BBBBBB',
            value: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Translation', () => {
        const patchObject = Object.assign(
          {
            value: 'BBBBBB',
          },
          new Translation()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Translation', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            entityType: 'BBBBBB',
            entityUuid: 'BBBBBB',
            value: 'BBBBBB',
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

      it('should delete a Translation', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addTranslationToCollectionIfMissing', () => {
        it('should add a Translation to an empty array', () => {
          const translation: ITranslation = { id: 123 };
          expectedResult = service.addTranslationToCollectionIfMissing([], translation);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(translation);
        });

        it('should not add a Translation to an array that contains it', () => {
          const translation: ITranslation = { id: 123 };
          const translationCollection: ITranslation[] = [
            {
              ...translation,
            },
            { id: 456 },
          ];
          expectedResult = service.addTranslationToCollectionIfMissing(translationCollection, translation);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Translation to an array that doesn't contain it", () => {
          const translation: ITranslation = { id: 123 };
          const translationCollection: ITranslation[] = [{ id: 456 }];
          expectedResult = service.addTranslationToCollectionIfMissing(translationCollection, translation);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(translation);
        });

        it('should add only unique Translation to an array', () => {
          const translationArray: ITranslation[] = [{ id: 123 }, { id: 456 }, { id: 98407 }];
          const translationCollection: ITranslation[] = [{ id: 123 }];
          expectedResult = service.addTranslationToCollectionIfMissing(translationCollection, ...translationArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const translation: ITranslation = { id: 123 };
          const translation2: ITranslation = { id: 456 };
          expectedResult = service.addTranslationToCollectionIfMissing([], translation, translation2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(translation);
          expect(expectedResult).toContain(translation2);
        });

        it('should accept null and undefined values', () => {
          const translation: ITranslation = { id: 123 };
          expectedResult = service.addTranslationToCollectionIfMissing([], null, translation, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(translation);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
