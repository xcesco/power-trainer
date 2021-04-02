jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IMeasureType, MeasureType } from '../measure-type.model';
import { MeasureTypeService } from '../service/measure-type.service';

import { MeasureTypeRoutingResolveService } from './measure-type-routing-resolve.service';

describe('Service Tests', () => {
  describe('MeasureType routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: MeasureTypeRoutingResolveService;
    let service: MeasureTypeService;
    let resultMeasureType: IMeasureType | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(MeasureTypeRoutingResolveService);
      service = TestBed.inject(MeasureTypeService);
      resultMeasureType = undefined;
    });

    describe('resolve', () => {
      it('should return IMeasureType returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMeasureType = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMeasureType).toEqual({ id: 123 });
      });

      it('should return new IMeasureType if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMeasureType = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultMeasureType).toEqual(new MeasureType());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMeasureType = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMeasureType).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
