import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DataUtils } from 'app/core/util/data-util.service';

import { MisurationDetailComponent } from './misuration-detail.component';

describe('Component Tests', () => {
  describe('Misuration Management Detail Component', () => {
    let comp: MisurationDetailComponent;
    let fixture: ComponentFixture<MisurationDetailComponent>;
    let dataUtils: DataUtils;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [MisurationDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ misuration: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(MisurationDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MisurationDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = TestBed.inject(DataUtils);
    });

    describe('OnInit', () => {
      it('Should load misuration on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.misuration).toEqual(jasmine.objectContaining({ id: 123 }));
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
