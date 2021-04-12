jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { WorkoutSheetService } from '../service/workout-sheet.service';
import { IWorkoutSheet, WorkoutSheet } from '../workout-sheet.model';

import { WorkoutSheetUpdateComponent } from './workout-sheet-update.component';

describe('Component Tests', () => {
  describe('WorkoutSheet Management Update Component', () => {
    let comp: WorkoutSheetUpdateComponent;
    let fixture: ComponentFixture<WorkoutSheetUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let workoutSheetService: WorkoutSheetService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [WorkoutSheetUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(WorkoutSheetUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(WorkoutSheetUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      workoutSheetService = TestBed.inject(WorkoutSheetService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const workoutSheet: IWorkoutSheet = { id: 456 };

        activatedRoute.data = of({ workoutSheet });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(workoutSheet));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const workoutSheet = { id: 123 };
        spyOn(workoutSheetService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ workoutSheet });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: workoutSheet }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(workoutSheetService.update).toHaveBeenCalledWith(workoutSheet);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const workoutSheet = new WorkoutSheet();
        spyOn(workoutSheetService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ workoutSheet });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: workoutSheet }));
        saveSubject.complete();

        // THEN
        expect(workoutSheetService.create).toHaveBeenCalledWith(workoutSheet);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const workoutSheet = { id: 123 };
        spyOn(workoutSheetService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ workoutSheet });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(workoutSheetService.update).toHaveBeenCalledWith(workoutSheet);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
