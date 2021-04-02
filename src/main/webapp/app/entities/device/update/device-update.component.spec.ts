jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DeviceService } from '../service/device.service';
import { IDevice, Device } from '../device.model';

import { DeviceUpdateComponent } from './device-update.component';

describe('Component Tests', () => {
  describe('Device Management Update Component', () => {
    let comp: DeviceUpdateComponent;
    let fixture: ComponentFixture<DeviceUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let deviceService: DeviceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DeviceUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DeviceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DeviceUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      deviceService = TestBed.inject(DeviceService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const device: IDevice = { id: 456 };

        activatedRoute.data = of({ device });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(device));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const device = { id: 123 };
        spyOn(deviceService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ device });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: device }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(deviceService.update).toHaveBeenCalledWith(device);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const device = new Device();
        spyOn(deviceService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ device });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: device }));
        saveSubject.complete();

        // THEN
        expect(deviceService.create).toHaveBeenCalledWith(device);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const device = { id: 123 };
        spyOn(deviceService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ device });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(deviceService.update).toHaveBeenCalledWith(device);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
