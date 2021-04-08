import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { MisurationComponent } from './list/misuration.component';
import { MisurationDetailComponent } from './detail/misuration-detail.component';
import { MisurationUpdateComponent } from './update/misuration-update.component';
import { MisurationDeleteDialogComponent } from './delete/misuration-delete-dialog.component';
import { MisurationRoutingModule } from './route/misuration-routing.module';

@NgModule({
  imports: [SharedModule, MisurationRoutingModule],
  declarations: [MisurationComponent, MisurationDetailComponent, MisurationUpdateComponent, MisurationDeleteDialogComponent],
  entryComponents: [MisurationDeleteDialogComponent],
})
export class MisurationModule {}
