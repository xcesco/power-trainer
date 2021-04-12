import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IExerciseValue } from '../exercise-value.model';

@Component({
  selector: 'jhi-exercise-value-detail',
  templateUrl: './exercise-value-detail.component.html',
})
export class ExerciseValueDetailComponent implements OnInit {
  exerciseValue: IExerciseValue | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ exerciseValue }) => {
      this.exerciseValue = exerciseValue;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
