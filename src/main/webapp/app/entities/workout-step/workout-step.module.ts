import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { WorkoutStepComponent } from './list/workout-step.component';
import { WorkoutStepDetailComponent } from './detail/workout-step-detail.component';
import { WorkoutStepUpdateComponent } from './update/workout-step-update.component';
import { WorkoutStepDeleteDialogComponent } from './delete/workout-step-delete-dialog.component';
import { WorkoutStepRoutingModule } from './route/workout-step-routing.module';

@NgModule({
  imports: [SharedModule, WorkoutStepRoutingModule],
  declarations: [WorkoutStepComponent, WorkoutStepDetailComponent, WorkoutStepUpdateComponent, WorkoutStepDeleteDialogComponent],
  entryComponents: [WorkoutStepDeleteDialogComponent],
})
export class WorkoutStepModule {}
