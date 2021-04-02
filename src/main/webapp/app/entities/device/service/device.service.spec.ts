import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDevice, Device } from '../device.model';

import { DeviceService } from './device.service';

describe('Service Tests', () => {
  describe('Device Service', () => {
    let service: DeviceService;
    let httpMock: HttpTestingController;
    let elemDefault: IDevice;
    let expectedResult: IDevice | IDevice[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DeviceService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        owner: 'AAAAAAA',
        deviceId: 'AAAAAAA',
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

      it('should create a Device', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Device()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Device', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            owner: 'BBBBBB',
            deviceId: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Device', () => {
        const patchObject = Object.assign({}, new Device());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Device', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            owner: 'BBBBBB',
            deviceId: 'BBBBBB',
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

      it('should delete a Device', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDeviceToCollectionIfMissing', () => {
        it('should add a Device to an empty array', () => {
          const device: IDevice = { id: 123 };
          expectedResult = service.addDeviceToCollectionIfMissing([], device);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(device);
        });

        it('should not add a Device to an array that contains it', () => {
          const device: IDevice = { id: 123 };
          const deviceCollection: IDevice[] = [
            {
              ...device,
            },
            { id: 456 },
          ];
          expectedResult = service.addDeviceToCollectionIfMissing(deviceCollection, device);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Device to an array that doesn't contain it", () => {
          const device: IDevice = { id: 123 };
          const deviceCollection: IDevice[] = [{ id: 456 }];
          expectedResult = service.addDeviceToCollectionIfMissing(deviceCollection, device);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(device);
        });

        it('should add only unique Device to an array', () => {
          const deviceArray: IDevice[] = [{ id: 123 }, { id: 456 }, { id: 86428 }];
          const deviceCollection: IDevice[] = [{ id: 123 }];
          expectedResult = service.addDeviceToCollectionIfMissing(deviceCollection, ...deviceArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const device: IDevice = { id: 123 };
          const device2: IDevice = { id: 456 };
          expectedResult = service.addDeviceToCollectionIfMissing([], device, device2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(device);
          expect(expectedResult).toContain(device2);
        });

        it('should accept null and undefined values', () => {
          const device: IDevice = { id: 123 };
          expectedResult = service.addDeviceToCollectionIfMissing([], null, device, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(device);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
