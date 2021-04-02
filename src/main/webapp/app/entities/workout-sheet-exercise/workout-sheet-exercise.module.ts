import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { WorkoutSheetExerciseComponent } from './list/workout-sheet-exercise.component';
import { WorkoutSheetExerciseDetailComponent } from './detail/workout-sheet-exercise-detail.component';
import { WorkoutSheetExerciseUpdateComponent } from './update/workout-sheet-exercise-update.component';
import { WorkoutSheetExerciseDeleteDialogComponent } from './delete/workout-sheet-exercise-delete-dialog.component';
import { WorkoutSheetExerciseRoutingModule } from './route/workout-sheet-exercise-routing.module';

@NgModule({
  imports: [SharedModule, WorkoutSheetExerciseRoutingModule],
  declarations: [
    WorkoutSheetExerciseComponent,
    WorkoutSheetExerciseDetailComponent,
    WorkoutSheetExerciseUpdateComponent,
    WorkoutSheetExerciseDeleteDialogComponent,
  ],
  entryComponents: [WorkoutSheetExerciseDeleteDialogComponent],
})
export class WorkoutSheetExerciseModule {}
