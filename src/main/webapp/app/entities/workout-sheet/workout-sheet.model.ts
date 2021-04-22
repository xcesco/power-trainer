import { IWorkoutSheetExercise } from 'app/entities/workout-sheet-exercise/workout-sheet-exercise.model';
import { WorkoutType } from 'app/entities/enumerations/workout-type.model';
import { v4 as uuidFunction } from 'uuid';

export interface IWorkoutSheet {
  id?: number;
  uuid?: string;
  name?: string;
  imageContentType?: string | null;
  image?: string | null;
  description?: string | null;
  owner?: string | null;
  prepareTime?: number | null;
  coolDownTime?: number | null;
  cycles?: number | null;
  cycleRestTime?: number | null;
  set?: number | null;
  setRestTime?: number | null;
  type?: WorkoutType | null;
  workoutSheetExercises?: IWorkoutSheetExercise[] | null;
}

export class WorkoutSheet implements IWorkoutSheet {
  constructor(
    public id?: number,
    public uuid?: string,
    public name?: string,
    public imageContentType?: string | null,
    public image?: string | null,
    public description?: string | null,
    public owner?: string | null,
    public prepareTime?: number | null,
    public coolDownTime?: number | null,
    public cycles?: number | null,
    public cycleRestTime?: number | null,
    public set?: number | null,
    public setRestTime?: number | null,
    public type?: WorkoutType | null,
    public workoutSheetExercises?: IWorkoutSheetExercise[] | null
  ) {
    if (!this.uuid) {
      this.uuid = uuidFunction().toString();
    }
  }
}

export function getWorkoutSheetIdentifier(workoutSheet: IWorkoutSheet): number | undefined {
  return workoutSheet.id;
}
