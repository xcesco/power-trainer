jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICalendar, Calendar } from '../calendar.model';
import { CalendarService } from '../service/calendar.service';

import { CalendarRoutingResolveService } from './calendar-routing-resolve.service';

describe('Service Tests', () => {
  describe('Calendar routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CalendarRoutingResolveService;
    let service: CalendarService;
    let resultCalendar: ICalendar | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CalendarRoutingResolveService);
      service = TestBed.inject(CalendarService);
      resultCalendar = undefined;
    });

    describe('resolve', () => {
      it('should return ICalendar returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCalendar = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCalendar).toEqual({ id: 123 });
      });

      it('should return new ICalendar if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCalendar = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCalendar).toEqual(new Calendar());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCalendar = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCalendar).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
