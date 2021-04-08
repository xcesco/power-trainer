import { IExerciseValue } from 'app/entities/exercise-value/exercise-value.model';
import { IMisuration } from 'app/entities/misuration/misuration.model';
import { IWorkout } from 'app/entities/workout/workout.model';

export interface ICalendar {
  id?: number;
  uuid?: string;
  name?: string;
  owner?: string;
  exerciseValues?: IExerciseValue[] | null;
  misurations?: IMisuration[] | null;
  workouts?: IWorkout[] | null;
}

export class Calendar implements ICalendar {
  constructor(
    public id?: number,
    public uuid?: string,
    public name?: string,
    public owner?: string,
    public exerciseValues?: IExerciseValue[] | null,
    public misurations?: IMisuration[] | null,
    public workouts?: IWorkout[] | null
  ) {}
}

export function getCalendarIdentifier(calendar: ICalendar): number | undefined {
  return calendar.id;
}
