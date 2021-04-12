import { IWorkoutSheet } from 'app/entities/workout-sheet/workout-sheet.model';
import { ValueType } from 'app/entities/enumerations/value-type.model';

export interface IWorkoutSheetExercise {
  id?: number;
  uuid?: string;
  order?: number | null;
  repetitions?: number | null;
  exerciseUuid?: string;
  exerciseName?: string;
  exerciseValue?: number;
  exerciseValueType?: ValueType;
  workoutSheet?: IWorkoutSheet | null;
}

export class WorkoutSheetExercise implements IWorkoutSheetExercise {
  constructor(
    public id?: number,
    public uuid?: string,
    public order?: number | null,
    public repetitions?: number | null,
    public exerciseUuid?: string,
    public exerciseName?: string,
    public exerciseValue?: number,
    public exerciseValueType?: ValueType,
    public workoutSheet?: IWorkoutSheet | null
  ) {}
}

export function getWorkoutSheetExerciseIdentifier(workoutSheetExercise: IWorkoutSheetExercise): number | undefined {
  return workoutSheetExercise.id;
}
