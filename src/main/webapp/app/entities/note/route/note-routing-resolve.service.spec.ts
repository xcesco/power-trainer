jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { INote, Note } from '../note.model';
import { NoteService } from '../service/note.service';

import { NoteRoutingResolveService } from './note-routing-resolve.service';

describe('Service Tests', () => {
  describe('Note routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: NoteRoutingResolveService;
    let service: NoteService;
    let resultNote: INote | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(NoteRoutingResolveService);
      service = TestBed.inject(NoteService);
      resultNote = undefined;
    });

    describe('resolve', () => {
      it('should return INote returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultNote = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultNote).toEqual({ id: 123 });
      });

      it('should return new INote if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultNote = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultNote).toEqual(new Note());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultNote = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultNote).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
