jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IWorkoutSheetExercise, WorkoutSheetExercise } from '../workout-sheet-exercise.model';
import { WorkoutSheetExerciseService } from '../service/workout-sheet-exercise.service';

import { WorkoutSheetExerciseRoutingResolveService } from './workout-sheet-exercise-routing-resolve.service';

describe('Service Tests', () => {
  describe('WorkoutSheetExercise routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: WorkoutSheetExerciseRoutingResolveService;
    let service: WorkoutSheetExerciseService;
    let resultWorkoutSheetExercise: IWorkoutSheetExercise | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(WorkoutSheetExerciseRoutingResolveService);
      service = TestBed.inject(WorkoutSheetExerciseService);
      resultWorkoutSheetExercise = undefined;
    });

    describe('resolve', () => {
      it('should return IWorkoutSheetExercise returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultWorkoutSheetExercise = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultWorkoutSheetExercise).toEqual({ id: 123 });
      });

      it('should return new IWorkoutSheetExercise if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultWorkoutSheetExercise = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultWorkoutSheetExercise).toEqual(new WorkoutSheetExercise());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultWorkoutSheetExercise = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultWorkoutSheetExercise).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
