import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ExerciseToolComponent } from './list/exercise-tool.component';
import { ExerciseToolDetailComponent } from './detail/exercise-tool-detail.component';
import { ExerciseToolUpdateComponent } from './update/exercise-tool-update.component';
import { ExerciseToolDeleteDialogComponent } from './delete/exercise-tool-delete-dialog.component';
import { ExerciseToolRoutingModule } from './route/exercise-tool-routing.module';

@NgModule({
  imports: [SharedModule, ExerciseToolRoutingModule],
  declarations: [ExerciseToolComponent, ExerciseToolDetailComponent, ExerciseToolUpdateComponent, ExerciseToolDeleteDialogComponent],
  entryComponents: [ExerciseToolDeleteDialogComponent],
})
export class ExerciseToolModule {}
