jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITranslation, Translation } from '../translation.model';
import { TranslationService } from '../service/translation.service';

import { TranslationRoutingResolveService } from './translation-routing-resolve.service';

describe('Service Tests', () => {
  describe('Translation routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: TranslationRoutingResolveService;
    let service: TranslationService;
    let resultTranslation: ITranslation | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(TranslationRoutingResolveService);
      service = TestBed.inject(TranslationService);
      resultTranslation = undefined;
    });

    describe('resolve', () => {
      it('should return ITranslation returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTranslation = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTranslation).toEqual({ id: 123 });
      });

      it('should return new ITranslation if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTranslation = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultTranslation).toEqual(new Translation());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTranslation = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTranslation).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
