import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { LanguageComponent } from './list/language.component';
import { LanguageDetailComponent } from './detail/language-detail.component';
import { LanguageUpdateComponent } from './update/language-update.component';
import { LanguageDeleteDialogComponent } from './delete/language-delete-dialog.component';
import { LanguageRoutingModule } from './route/language-routing.module';

@NgModule({
  imports: [SharedModule, LanguageRoutingModule],
  declarations: [LanguageComponent, LanguageDetailComponent, LanguageUpdateComponent, LanguageDeleteDialogComponent],
  entryComponents: [LanguageDeleteDialogComponent],
})
export class LanguageModule {}
