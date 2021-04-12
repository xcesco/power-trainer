jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IExerciseResource, ExerciseResource } from '../exercise-resource.model';
import { ExerciseResourceService } from '../service/exercise-resource.service';

import { ExerciseResourceRoutingResolveService } from './exercise-resource-routing-resolve.service';

describe('Service Tests', () => {
  describe('ExerciseResource routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ExerciseResourceRoutingResolveService;
    let service: ExerciseResourceService;
    let resultExerciseResource: IExerciseResource | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ExerciseResourceRoutingResolveService);
      service = TestBed.inject(ExerciseResourceService);
      resultExerciseResource = undefined;
    });

    describe('resolve', () => {
      it('should return IExerciseResource returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultExerciseResource = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultExerciseResource).toEqual({ id: 123 });
      });

      it('should return new IExerciseResource if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultExerciseResource = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultExerciseResource).toEqual(new ExerciseResource());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultExerciseResource = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultExerciseResource).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
