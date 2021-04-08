import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILanguage, Language } from '../language.model';

import { LanguageService } from './language.service';

describe('Service Tests', () => {
  describe('Language Service', () => {
    let service: LanguageService;
    let httpMock: HttpTestingController;
    let elemDefault: ILanguage;
    let expectedResult: ILanguage | ILanguage[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(LanguageService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        code: 'AAAAAAA',
        name: 'AAAAAAA',
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

      it('should create a Language', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Language()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Language', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            code: 'BBBBBB',
            name: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Language', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
          },
          new Language()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Language', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            code: 'BBBBBB',
            name: 'BBBBBB',
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

      it('should delete a Language', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addLanguageToCollectionIfMissing', () => {
        it('should add a Language to an empty array', () => {
          const language: ILanguage = { id: 123 };
          expectedResult = service.addLanguageToCollectionIfMissing([], language);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(language);
        });

        it('should not add a Language to an array that contains it', () => {
          const language: ILanguage = { id: 123 };
          const languageCollection: ILanguage[] = [
            {
              ...language,
            },
            { id: 456 },
          ];
          expectedResult = service.addLanguageToCollectionIfMissing(languageCollection, language);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Language to an array that doesn't contain it", () => {
          const language: ILanguage = { id: 123 };
          const languageCollection: ILanguage[] = [{ id: 456 }];
          expectedResult = service.addLanguageToCollectionIfMissing(languageCollection, language);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(language);
        });

        it('should add only unique Language to an array', () => {
          const languageArray: ILanguage[] = [{ id: 123 }, { id: 456 }, { id: 35567 }];
          const languageCollection: ILanguage[] = [{ id: 123 }];
          expectedResult = service.addLanguageToCollectionIfMissing(languageCollection, ...languageArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const language: ILanguage = { id: 123 };
          const language2: ILanguage = { id: 456 };
          expectedResult = service.addLanguageToCollectionIfMissing([], language, language2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(language);
          expect(expectedResult).toContain(language2);
        });

        it('should accept null and undefined values', () => {
          const language: ILanguage = { id: 123 };
          expectedResult = service.addLanguageToCollectionIfMissing([], null, language, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(language);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
