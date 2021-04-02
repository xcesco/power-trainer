import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DataUtils } from 'app/core/util/data-util.service';

import { MuscleDetailComponent } from './muscle-detail.component';

describe('Component Tests', () => {
  describe('Muscle Management Detail Component', () => {
    let comp: MuscleDetailComponent;
    let fixture: ComponentFixture<MuscleDetailComponent>;
    let dataUtils: DataUtils;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [MuscleDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ muscle: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(MuscleDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MuscleDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = TestBed.inject(DataUtils);
    });

    describe('OnInit', () => {
      it('Should load muscle on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.muscle).toEqual(jasmine.objectContaining({ id: 123 }));
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
