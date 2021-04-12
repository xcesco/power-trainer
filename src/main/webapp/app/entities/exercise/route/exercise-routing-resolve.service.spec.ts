jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IExercise, Exercise } from '../exercise.model';
import { ExerciseService } from '../service/exercise.service';

import { ExerciseRoutingResolveService } from './exercise-routing-resolve.service';

describe('Service Tests', () => {
  describe('Exercise routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ExerciseRoutingResolveService;
    let service: ExerciseService;
    let resultExercise: IExercise | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ExerciseRoutingResolveService);
      service = TestBed.inject(ExerciseService);
      resultExercise = undefined;
    });

    describe('resolve', () => {
      it('should return IExercise returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultExercise = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultExercise).toEqual({ id: 123 });
      });

      it('should return new IExercise if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultExercise = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultExercise).toEqual(new Exercise());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultExercise = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultExercise).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
