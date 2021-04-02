import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMuscle2Exercise } from '../muscle-2-exercise.model';

@Component({
  selector: 'jhi-muscle-2-exercise-detail',
  templateUrl: './muscle-2-exercise-detail.component.html',
})
export class Muscle2ExerciseDetailComponent implements OnInit {
  muscle2Exercise: IMuscle2Exercise | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ muscle2Exercise }) => {
      this.muscle2Exercise = muscle2Exercise;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
