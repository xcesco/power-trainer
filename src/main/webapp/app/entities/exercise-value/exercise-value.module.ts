import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ExerciseValueComponent } from './list/exercise-value.component';
import { ExerciseValueDetailComponent } from './detail/exercise-value-detail.component';
import { ExerciseValueUpdateComponent } from './update/exercise-value-update.component';
import { ExerciseValueDeleteDialogComponent } from './delete/exercise-value-delete-dialog.component';
import { ExerciseValueRoutingModule } from './route/exercise-value-routing.module';

@NgModule({
  imports: [SharedModule, ExerciseValueRoutingModule],
  declarations: [ExerciseValueComponent, ExerciseValueDetailComponent, ExerciseValueUpdateComponent, ExerciseValueDeleteDialogComponent],
  entryComponents: [ExerciseValueDeleteDialogComponent],
})
export class ExerciseValueModule {}
