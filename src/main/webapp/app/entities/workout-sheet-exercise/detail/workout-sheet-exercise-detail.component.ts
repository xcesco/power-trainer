import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWorkoutSheetExercise } from '../workout-sheet-exercise.model';

@Component({
  selector: 'jhi-workout-sheet-exercise-detail',
  templateUrl: './workout-sheet-exercise-detail.component.html',
})
export class WorkoutSheetExerciseDetailComponent implements OnInit {
  workoutSheetExercise: IWorkoutSheetExercise | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ workoutSheetExercise }) => {
      this.workoutSheetExercise = workoutSheetExercise;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
