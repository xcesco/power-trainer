jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { Muscle2ExerciseService } from '../service/muscle-2-exercise.service';

import { Muscle2ExerciseDeleteDialogComponent } from './muscle-2-exercise-delete-dialog.component';

describe('Component Tests', () => {
  describe('Muscle2Exercise Management Delete Component', () => {
    let comp: Muscle2ExerciseDeleteDialogComponent;
    let fixture: ComponentFixture<Muscle2ExerciseDeleteDialogComponent>;
    let service: Muscle2ExerciseService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [Muscle2ExerciseDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(Muscle2ExerciseDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(Muscle2ExerciseDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(Muscle2ExerciseService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
