import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { Muscle2ExerciseComponent } from './list/muscle-2-exercise.component';
import { Muscle2ExerciseDetailComponent } from './detail/muscle-2-exercise-detail.component';
import { Muscle2ExerciseUpdateComponent } from './update/muscle-2-exercise-update.component';
import { Muscle2ExerciseDeleteDialogComponent } from './delete/muscle-2-exercise-delete-dialog.component';
import { Muscle2ExerciseRoutingModule } from './route/muscle-2-exercise-routing.module';

@NgModule({
  imports: [SharedModule, Muscle2ExerciseRoutingModule],
  declarations: [
    Muscle2ExerciseComponent,
    Muscle2ExerciseDetailComponent,
    Muscle2ExerciseUpdateComponent,
    Muscle2ExerciseDeleteDialogComponent,
  ],
  entryComponents: [Muscle2ExerciseDeleteDialogComponent],
})
export class Muscle2ExerciseModule {}
