import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ExerciseResourceComponent } from './list/exercise-resource.component';
import { ExerciseResourceDetailComponent } from './detail/exercise-resource-detail.component';
import { ExerciseResourceUpdateComponent } from './update/exercise-resource-update.component';
import { ExerciseResourceDeleteDialogComponent } from './delete/exercise-resource-delete-dialog.component';
import { ExerciseResourceRoutingModule } from './route/exercise-resource-routing.module';

@NgModule({
  imports: [SharedModule, ExerciseResourceRoutingModule],
  declarations: [
    ExerciseResourceComponent,
    ExerciseResourceDetailComponent,
    ExerciseResourceUpdateComponent,
    ExerciseResourceDeleteDialogComponent,
  ],
  entryComponents: [ExerciseResourceDeleteDialogComponent],
})
export class ExerciseResourceModule {}
