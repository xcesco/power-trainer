import * as dayjs from 'dayjs';
import { WorkoutType } from 'app/entities/enumerations/workout-type.model';
import { WorkoutStatus } from 'app/entities/enumerations/workout-status.model';

export interface IWorkout {
  id?: number;
  uuid?: string;
  name?: string | null;
  imageContentType?: string | null;
  image?: string | null;
  type?: WorkoutType | null;
  executionTime?: number | null;
  previewTime?: number | null;
  status?: WorkoutStatus | null;
  date?: dayjs.Dayjs | null;
  note?: string | null;
}

export class Workout implements IWorkout {
  constructor(
    public id?: number,
    public uuid?: string,
    public name?: string | null,
    public imageContentType?: string | null,
    public image?: string | null,
    public type?: WorkoutType | null,
    public executionTime?: number | null,
    public previewTime?: number | null,
    public status?: WorkoutStatus | null,
    public date?: dayjs.Dayjs | null,
    public note?: string | null
  ) {}
}

export function getWorkoutIdentifier(workout: IWorkout): number | undefined {
  return workout.id;
}
