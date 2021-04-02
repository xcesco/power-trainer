import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWorkoutStep } from '../workout-step.model';

@Component({
  selector: 'jhi-workout-step-detail',
  templateUrl: './workout-step-detail.component.html',
})
export class WorkoutStepDetailComponent implements OnInit {
  workoutStep: IWorkoutStep | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ workoutStep }) => {
      this.workoutStep = workoutStep;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
