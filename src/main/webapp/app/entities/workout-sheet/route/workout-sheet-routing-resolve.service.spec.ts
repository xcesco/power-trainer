jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IWorkoutSheet, WorkoutSheet } from '../workout-sheet.model';
import { WorkoutSheetService } from '../service/workout-sheet.service';

import { WorkoutSheetRoutingResolveService } from './workout-sheet-routing-resolve.service';

describe('Service Tests', () => {
  describe('WorkoutSheet routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: WorkoutSheetRoutingResolveService;
    let service: WorkoutSheetService;
    let resultWorkoutSheet: IWorkoutSheet | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(WorkoutSheetRoutingResolveService);
      service = TestBed.inject(WorkoutSheetService);
      resultWorkoutSheet = undefined;
    });

    describe('resolve', () => {
      it('should return IWorkoutSheet returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultWorkoutSheet = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultWorkoutSheet).toEqual({ id: 123 });
      });

      it('should return new IWorkoutSheet if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultWorkoutSheet = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultWorkoutSheet).toEqual(new WorkoutSheet());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultWorkoutSheet = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultWorkoutSheet).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
