jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IMeasureValue, MeasureValue } from '../measure-value.model';
import { MeasureValueService } from '../service/measure-value.service';

import { MeasureValueRoutingResolveService } from './measure-value-routing-resolve.service';

describe('Service Tests', () => {
  describe('MeasureValue routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: MeasureValueRoutingResolveService;
    let service: MeasureValueService;
    let resultMeasureValue: IMeasureValue | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(MeasureValueRoutingResolveService);
      service = TestBed.inject(MeasureValueService);
      resultMeasureValue = undefined;
    });

    describe('resolve', () => {
      it('should return IMeasureValue returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMeasureValue = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMeasureValue).toEqual({ id: 123 });
      });

      it('should return new IMeasureValue if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMeasureValue = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultMeasureValue).toEqual(new MeasureValue());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMeasureValue = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMeasureValue).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
