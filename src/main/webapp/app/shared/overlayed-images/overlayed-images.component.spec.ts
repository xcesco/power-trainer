import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OverlayedImagesComponent } from './overlayed-images.component';

describe('OverlayedImagesComponent', () => {
  let component: OverlayedImagesComponent;
  let fixture: ComponentFixture<OverlayedImagesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [OverlayedImagesComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OverlayedImagesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
