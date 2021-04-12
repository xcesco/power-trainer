jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ExerciseResourceService } from '../service/exercise-resource.service';

import { ExerciseResourceDeleteDialogComponent } from './exercise-resource-delete-dialog.component';

describe('Component Tests', () => {
  describe('ExerciseResource Management Delete Component', () => {
    let comp: ExerciseResourceDeleteDialogComponent;
    let fixture: ComponentFixture<ExerciseResourceDeleteDialogComponent>;
    let service: ExerciseResourceService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ExerciseResourceDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(ExerciseResourceDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ExerciseResourceDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ExerciseResourceService);
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
