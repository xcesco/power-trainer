import { AfterViewInit, Component, Input, OnInit, Optional, Self } from '@angular/core';
import { IImage } from 'app/shared/shared.model';
import { ControlValueAccessor, FormControl, NG_VALUE_ACCESSOR, NgControl } from '@angular/forms';

@Component({
  selector: 'jhi-overlayed-images',
  templateUrl: './overlayed-images.component.html',
  styleUrls: ['./overlayed-images.component.scss'],
})
export class OverlayedImagesComponent implements OnInit {
  @Input() baseImageSource: string;

  @Input() images: IImage[] | null | undefined;

  touched = false;

  disabled = false;

  constructor() {
    this.images = [];
    this.baseImageSource = 'content/images/muscles/muscles_base.jpg';
  }

  onChange = (images: IImage[] | null | undefined): void => {
    this.images = images;
  };

  onTouched = (): void => {
    this.touched = true;
  };

  ngOnInit(): void {
    console.error('');
  }
}
