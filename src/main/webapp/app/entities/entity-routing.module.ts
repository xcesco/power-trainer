import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
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
        path: 'measure-value',
        data: { pageTitle: 'powerTrainerApp.measureValue.home.title' },
        loadChildren: () => import('./measure-value/measure-value.module').then(m => m.MeasureValueModule),
      },
      {
        path: 'measure-type',
        data: { pageTitle: 'powerTrainerApp.measureType.home.title' },
        loadChildren: () => import('./measure-type/measure-type.module').then(m => m.MeasureTypeModule),
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
        path: 'muscle-2-exercise',
        data: { pageTitle: 'powerTrainerApp.muscle2Exercise.home.title' },
        loadChildren: () => import('./muscle-2-exercise/muscle-2-exercise.module').then(m => m.Muscle2ExerciseModule),
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
        path: 'note',
        data: { pageTitle: 'powerTrainerApp.note.home.title' },
        loadChildren: () => import('./note/note.module').then(m => m.NoteModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
