import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { MisurationTypeComponent } from './list/misuration-type.component';
import { MisurationTypeDetailComponent } from './detail/misuration-type-detail.component';
import { MisurationTypeUpdateComponent } from './update/misuration-type-update.component';
import { MisurationTypeDeleteDialogComponent } from './delete/misuration-type-delete-dialog.component';
import { MisurationTypeRoutingModule } from './route/misuration-type-routing.module';

@NgModule({
  imports: [SharedModule, MisurationTypeRoutingModule],
  declarations: [
    MisurationTypeComponent,
    MisurationTypeDetailComponent,
    MisurationTypeUpdateComponent,
    MisurationTypeDeleteDialogComponent,
  ],
  entryComponents: [MisurationTypeDeleteDialogComponent],
})
export class MisurationTypeModule {}
