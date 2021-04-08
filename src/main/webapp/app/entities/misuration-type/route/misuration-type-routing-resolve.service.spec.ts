jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IMisurationType, MisurationType } from '../misuration-type.model';
import { MisurationTypeService } from '../service/misuration-type.service';

import { MisurationTypeRoutingResolveService } from './misuration-type-routing-resolve.service';

describe('Service Tests', () => {
  describe('MisurationType routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: MisurationTypeRoutingResolveService;
    let service: MisurationTypeService;
    let resultMisurationType: IMisurationType | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(MisurationTypeRoutingResolveService);
      service = TestBed.inject(MisurationTypeService);
      resultMisurationType = undefined;
    });

    describe('resolve', () => {
      it('should return IMisurationType returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMisurationType = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMisurationType).toEqual({ id: 123 });
      });

      it('should return new IMisurationType if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMisurationType = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultMisurationType).toEqual(new MisurationType());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMisurationType = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMisurationType).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
