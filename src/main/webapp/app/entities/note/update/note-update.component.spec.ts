jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { NoteService } from '../service/note.service';
import { INote, Note } from '../note.model';
import { IExercise } from 'app/entities/exercise/exercise.model';
import { ExerciseService } from 'app/entities/exercise/service/exercise.service';

import { NoteUpdateComponent } from './note-update.component';

describe('Component Tests', () => {
  describe('Note Management Update Component', () => {
    let comp: NoteUpdateComponent;
    let fixture: ComponentFixture<NoteUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let noteService: NoteService;
    let exerciseService: ExerciseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [NoteUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(NoteUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(NoteUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      noteService = TestBed.inject(NoteService);
      exerciseService = TestBed.inject(ExerciseService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Exercise query and add missing value', () => {
        const note: INote = { id: 456 };
        const exercise: IExercise = { id: 75307 };
        note.exercise = exercise;

        const exerciseCollection: IExercise[] = [{ id: 43974 }];
        spyOn(exerciseService, 'query').and.returnValue(of(new HttpResponse({ body: exerciseCollection })));
        const additionalExercises = [exercise];
        const expectedCollection: IExercise[] = [...additionalExercises, ...exerciseCollection];
        spyOn(exerciseService, 'addExerciseToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ note });
        comp.ngOnInit();

        expect(exerciseService.query).toHaveBeenCalled();
        expect(exerciseService.addExerciseToCollectionIfMissing).toHaveBeenCalledWith(exerciseCollection, ...additionalExercises);
        expect(comp.exercisesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const note: INote = { id: 456 };
        const exercise: IExercise = { id: 28518 };
        note.exercise = exercise;

        activatedRoute.data = of({ note });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(note));
        expect(comp.exercisesSharedCollection).toContain(exercise);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const note = { id: 123 };
        spyOn(noteService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ note });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: note }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(noteService.update).toHaveBeenCalledWith(note);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const note = new Note();
        spyOn(noteService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ note });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: note }));
        saveSubject.complete();

        // THEN
        expect(noteService.create).toHaveBeenCalledWith(note);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const note = { id: 123 };
        spyOn(noteService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ note });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(noteService.update).toHaveBeenCalledWith(note);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackExerciseById', () => {
        it('Should return tracked Exercise primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackExerciseById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
