import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMisuration, Misuration } from '../misuration.model';

import { MisurationService } from './misuration.service';

describe('Service Tests', () => {
  describe('Misuration Service', () => {
    let service: MisurationService;
    let httpMock: HttpTestingController;
    let elemDefault: IMisuration;
    let expectedResult: IMisuration | IMisuration[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(MisurationService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        uuid: 'AAAAAAA',
        date: currentDate,
        value: 0,
        imageContentType: 'image/png',
        image: 'AAAAAAA',
        note: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            date: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Misuration', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            date: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.create(new Misuration()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Misuration', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uuid: 'BBBBBB',
            date: currentDate.format(DATE_TIME_FORMAT),
            value: 1,
            image: 'BBBBBB',
            note: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Misuration', () => {
        const patchObject = Object.assign({}, new Misuration());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Misuration', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            uuid: 'BBBBBB',
            date: currentDate.format(DATE_TIME_FORMAT),
            value: 1,
            image: 'BBBBBB',
            note: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Misuration', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addMisurationToCollectionIfMissing', () => {
        it('should add a Misuration to an empty array', () => {
          const misuration: IMisuration = { id: 123 };
          expectedResult = service.addMisurationToCollectionIfMissing([], misuration);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(misuration);
        });

        it('should not add a Misuration to an array that contains it', () => {
          const misuration: IMisuration = { id: 123 };
          const misurationCollection: IMisuration[] = [
            {
              ...misuration,
            },
            { id: 456 },
          ];
          expectedResult = service.addMisurationToCollectionIfMissing(misurationCollection, misuration);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Misuration to an array that doesn't contain it", () => {
          const misuration: IMisuration = { id: 123 };
          const misurationCollection: IMisuration[] = [{ id: 456 }];
          expectedResult = service.addMisurationToCollectionIfMissing(misurationCollection, misuration);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(misuration);
        });

        it('should add only unique Misuration to an array', () => {
          const misurationArray: IMisuration[] = [{ id: 123 }, { id: 456 }, { id: 11834 }];
          const misurationCollection: IMisuration[] = [{ id: 123 }];
          expectedResult = service.addMisurationToCollectionIfMissing(misurationCollection, ...misurationArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const misuration: IMisuration = { id: 123 };
          const misuration2: IMisuration = { id: 456 };
          expectedResult = service.addMisurationToCollectionIfMissing([], misuration, misuration2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(misuration);
          expect(expectedResult).toContain(misuration2);
        });

        it('should accept null and undefined values', () => {
          const misuration: IMisuration = { id: 123 };
          expectedResult = service.addMisurationToCollectionIfMissing([], null, misuration, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(misuration);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
