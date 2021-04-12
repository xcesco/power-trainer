import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMisurationType } from '../misuration-type.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-misuration-type-detail',
  templateUrl: './misuration-type-detail.component.html',
})
export class MisurationTypeDetailComponent implements OnInit {
  misurationType: IMisurationType | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ misurationType }) => {
      this.misurationType = misurationType;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
