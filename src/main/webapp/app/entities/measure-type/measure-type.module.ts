import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { MeasureTypeComponent } from './list/measure-type.component';
import { MeasureTypeDetailComponent } from './detail/measure-type-detail.component';
import { MeasureTypeUpdateComponent } from './update/measure-type-update.component';
import { MeasureTypeDeleteDialogComponent } from './delete/measure-type-delete-dialog.component';
import { MeasureTypeRoutingModule } from './route/measure-type-routing.module';

@NgModule({
  imports: [SharedModule, MeasureTypeRoutingModule],
  declarations: [MeasureTypeComponent, MeasureTypeDetailComponent, MeasureTypeUpdateComponent, MeasureTypeDeleteDialogComponent],
  entryComponents: [MeasureTypeDeleteDialogComponent],
})
export class MeasureTypeModule {}
