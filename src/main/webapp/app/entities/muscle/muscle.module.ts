import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { MuscleComponent } from './list/muscle.component';
import { MuscleDetailComponent } from './detail/muscle-detail.component';
import { MuscleUpdateComponent } from './update/muscle-update.component';
import { MuscleDeleteDialogComponent } from './delete/muscle-delete-dialog.component';
import { MuscleRoutingModule } from './route/muscle-routing.module';

@NgModule({
  imports: [SharedModule, MuscleRoutingModule],
  declarations: [MuscleComponent, MuscleDetailComponent, MuscleUpdateComponent, MuscleDeleteDialogComponent],
  entryComponents: [MuscleDeleteDialogComponent],
})
export class MuscleModule {}
