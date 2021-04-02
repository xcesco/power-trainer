import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { MeasureValueComponent } from './list/measure-value.component';
import { MeasureValueDetailComponent } from './detail/measure-value-detail.component';
import { MeasureValueUpdateComponent } from './update/measure-value-update.component';
import { MeasureValueDeleteDialogComponent } from './delete/measure-value-delete-dialog.component';
import { MeasureValueRoutingModule } from './route/measure-value-routing.module';

@NgModule({
  imports: [SharedModule, MeasureValueRoutingModule],
  declarations: [MeasureValueComponent, MeasureValueDetailComponent, MeasureValueUpdateComponent, MeasureValueDeleteDialogComponent],
  entryComponents: [MeasureValueDeleteDialogComponent],
})
export class MeasureValueModule {}
