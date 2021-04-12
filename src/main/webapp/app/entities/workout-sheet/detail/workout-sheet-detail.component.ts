import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWorkoutSheet } from '../workout-sheet.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-workout-sheet-detail',
  templateUrl: './workout-sheet-detail.component.html',
})
export class WorkoutSheetDetailComponent implements OnInit {
  workoutSheet: IWorkoutSheet | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ workoutSheet }) => {
      this.workoutSheet = workoutSheet;
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
