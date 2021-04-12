import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IExerciseResource } from '../exercise-resource.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-exercise-resource-detail',
  templateUrl: './exercise-resource-detail.component.html',
})
export class ExerciseResourceDetailComponent implements OnInit {
  exerciseResource: IExerciseResource | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ exerciseResource }) => {
      this.exerciseResource = exerciseResource;
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
