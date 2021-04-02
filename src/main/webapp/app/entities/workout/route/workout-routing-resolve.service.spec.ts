jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IWorkout, Workout } from '../workout.model';
import { WorkoutService } from '../service/workout.service';

import { WorkoutRoutingResolveService } from './workout-routing-resolve.service';

describe('Service Tests', () => {
  describe('Workout routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: WorkoutRoutingResolveService;
    let service: WorkoutService;
    let resultWorkout: IWorkout | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(WorkoutRoutingResolveService);
      service = TestBed.inject(WorkoutService);
      resultWorkout = undefined;
    });

    describe('resolve', () => {
      it('should return IWorkout returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultWorkout = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultWorkout).toEqual({ id: 123 });
      });

      it('should return new IWorkout if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultWorkout = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultWorkout).toEqual(new Workout());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultWorkout = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultWorkout).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
