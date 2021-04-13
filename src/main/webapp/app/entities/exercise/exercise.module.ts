import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ExerciseComponent } from './list/exercise.component';
import { ExerciseDetailComponent } from './detail/exercise-detail.component';
import { ExerciseUpdateComponent } from './update/exercise-update.component';
import { ExerciseDeleteDialogComponent } from './delete/exercise-delete-dialog.component';
import { ExerciseRoutingModule } from './route/exercise-routing.module';
import { MarkdownModule } from 'ngx-markdown';
import { QuillModule } from 'ngx-quill';
import { ExpandableAreaModule } from 'ng-expandable-area';

@NgModule({
  imports: [SharedModule, ExerciseRoutingModule, MarkdownModule.forRoot(), QuillModule.forRoot(), ExpandableAreaModule],
  declarations: [ExerciseComponent, ExerciseDetailComponent, ExerciseUpdateComponent, ExerciseDeleteDialogComponent],
  entryComponents: [ExerciseDeleteDialogComponent],
})
export class ExerciseModule {}
