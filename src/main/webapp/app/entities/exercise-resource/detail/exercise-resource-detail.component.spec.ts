import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DataUtils } from 'app/core/util/data-util.service';

import { ExerciseResourceDetailComponent } from './exercise-resource-detail.component';

describe('Component Tests', () => {
  describe('ExerciseResource Management Detail Component', () => {
    let comp: ExerciseResourceDetailComponent;
    let fixture: ComponentFixture<ExerciseResourceDetailComponent>;
    let dataUtils: DataUtils;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ExerciseResourceDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ exerciseResource: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ExerciseResourceDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ExerciseResourceDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = TestBed.inject(DataUtils);
    });

    describe('OnInit', () => {
      it('Should load exerciseResource on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.exerciseResource).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from DataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from DataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeBase64, fakeContentType);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeBase64, fakeContentType);
      });
    });
  });
});
