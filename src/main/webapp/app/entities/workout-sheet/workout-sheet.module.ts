import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { WorkoutSheetComponent } from './list/workout-sheet.component';
import { WorkoutSheetDetailComponent } from './detail/workout-sheet-detail.component';
import { WorkoutSheetUpdateComponent } from './update/workout-sheet-update.component';
import { WorkoutSheetDeleteDialogComponent } from './delete/workout-sheet-delete-dialog.component';
import { WorkoutSheetRoutingModule } from './route/workout-sheet-routing.module';

@NgModule({
  imports: [SharedModule, WorkoutSheetRoutingModule],
  declarations: [WorkoutSheetComponent, WorkoutSheetDetailComponent, WorkoutSheetUpdateComponent, WorkoutSheetDeleteDialogComponent],
  entryComponents: [WorkoutSheetDeleteDialogComponent],
})
export class WorkoutSheetModule {}
