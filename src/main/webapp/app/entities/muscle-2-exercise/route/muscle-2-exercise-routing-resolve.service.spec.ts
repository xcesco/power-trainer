jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IMuscle2Exercise, Muscle2Exercise } from '../muscle-2-exercise.model';
import { Muscle2ExerciseService } from '../service/muscle-2-exercise.service';

import { Muscle2ExerciseRoutingResolveService } from './muscle-2-exercise-routing-resolve.service';

describe('Service Tests', () => {
  describe('Muscle2Exercise routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: Muscle2ExerciseRoutingResolveService;
    let service: Muscle2ExerciseService;
    let resultMuscle2Exercise: IMuscle2Exercise | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(Muscle2ExerciseRoutingResolveService);
      service = TestBed.inject(Muscle2ExerciseService);
      resultMuscle2Exercise = undefined;
    });

    describe('resolve', () => {
      it('should return IMuscle2Exercise returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMuscle2Exercise = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMuscle2Exercise).toEqual({ id: 123 });
      });

      it('should return new IMuscle2Exercise if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMuscle2Exercise = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultMuscle2Exercise).toEqual(new Muscle2Exercise());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMuscle2Exercise = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMuscle2Exercise).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
