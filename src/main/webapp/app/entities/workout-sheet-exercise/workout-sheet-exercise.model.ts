import { ValueType } from 'app/entities/enumerations/value-type.model';

export interface IWorkoutSheetExercise {
  id?: number;
  uuid?: string;
  order?: number | null;
  repetition?: number | null;
  value?: number | null;
  valueType?: ValueType | null;
}

export class WorkoutSheetExercise implements IWorkoutSheetExercise {
  constructor(
    public id?: number,
    public uuid?: string,
    public order?: number | null,
    public repetition?: number | null,
    public value?: number | null,
    public valueType?: ValueType | null
  ) {}
}

export function getWorkoutSheetExerciseIdentifier(workoutSheetExercise: IWorkoutSheetExercise): number | undefined {
  return workoutSheetExercise.id;
}
