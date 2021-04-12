import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'language',
        data: { pageTitle: 'powerTrainerApp.language.home.title' },
        loadChildren: () => import('./language/language.module').then(m => m.LanguageModule),
      },
      {
        path: 'translation',
        data: { pageTitle: 'powerTrainerApp.translation.home.title' },
        loadChildren: () => import('./translation/translation.module').then(m => m.TranslationModule),
      },
      {
        path: 'calendar',
        data: { pageTitle: 'powerTrainerApp.calendar.home.title' },
        loadChildren: () => import('./calendar/calendar.module').then(m => m.CalendarModule),
      },
      {
        path: 'exercise',
        data: { pageTitle: 'powerTrainerApp.exercise.home.title' },
        loadChildren: () => import('./exercise/exercise.module').then(m => m.ExerciseModule),
      },
      {
        path: 'exercise-value',
        data: { pageTitle: 'powerTrainerApp.exerciseValue.home.title' },
        loadChildren: () => import('./exercise-value/exercise-value.module').then(m => m.ExerciseValueModule),
      },
      {
        path: 'exercise-tool',
        data: { pageTitle: 'powerTrainerApp.exerciseTool.home.title' },
        loadChildren: () => import('./exercise-tool/exercise-tool.module').then(m => m.ExerciseToolModule),
      },
      {
        path: 'misuration',
        data: { pageTitle: 'powerTrainerApp.misuration.home.title' },
        loadChildren: () => import('./misuration/misuration.module').then(m => m.MisurationModule),
      },
      {
        path: 'misuration-type',
        data: { pageTitle: 'powerTrainerApp.misurationType.home.title' },
        loadChildren: () => import('./misuration-type/misuration-type.module').then(m => m.MisurationTypeModule),
      },
      {
        path: 'workout-sheet',
        data: { pageTitle: 'powerTrainerApp.workoutSheet.home.title' },
        loadChildren: () => import('./workout-sheet/workout-sheet.module').then(m => m.WorkoutSheetModule),
      },
      {
        path: 'workout-sheet-exercise',
        data: { pageTitle: 'powerTrainerApp.workoutSheetExercise.home.title' },
        loadChildren: () => import('./workout-sheet-exercise/workout-sheet-exercise.module').then(m => m.WorkoutSheetExerciseModule),
      },
      {
        path: 'workout',
        data: { pageTitle: 'powerTrainerApp.workout.home.title' },
        loadChildren: () => import('./workout/workout.module').then(m => m.WorkoutModule),
      },
      {
        path: 'workout-step',
        data: { pageTitle: 'powerTrainerApp.workoutStep.home.title' },
        loadChildren: () => import('./workout-step/workout-step.module').then(m => m.WorkoutStepModule),
      },
      {
        path: 'device',
        data: { pageTitle: 'powerTrainerApp.device.home.title' },
        loadChildren: () => import('./device/device.module').then(m => m.DeviceModule),
      },
      {
        path: 'muscle',
        data: { pageTitle: 'powerTrainerApp.muscle.home.title' },
        loadChildren: () => import('./muscle/muscle.module').then(m => m.MuscleModule),
      },
      {
        path: 'exercise-resource',
        data: { pageTitle: 'powerTrainerApp.exerciseResource.home.title' },
        loadChildren: () => import('./exercise-resource/exercise-resource.module').then(m => m.ExerciseResourceModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
