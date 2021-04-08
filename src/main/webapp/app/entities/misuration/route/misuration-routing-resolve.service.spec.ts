jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IMisuration, Misuration } from '../misuration.model';
import { MisurationService } from '../service/misuration.service';

import { MisurationRoutingResolveService } from './misuration-routing-resolve.service';

describe('Service Tests', () => {
  describe('Misuration routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: MisurationRoutingResolveService;
    let service: MisurationService;
    let resultMisuration: IMisuration | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(MisurationRoutingResolveService);
      service = TestBed.inject(MisurationService);
      resultMisuration = undefined;
    });

    describe('resolve', () => {
      it('should return IMisuration returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMisuration = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMisuration).toEqual({ id: 123 });
      });

      it('should return new IMisuration if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMisuration = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultMisuration).toEqual(new Misuration());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMisuration = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMisuration).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
