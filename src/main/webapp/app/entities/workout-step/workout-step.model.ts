import { IWorkout } from 'app/entities/workout/workout.model';
import { WorkoutStepType } from 'app/entities/enumerations/workout-step-type.model';
import { WorkoutStatus } from 'app/entities/enumerations/workout-status.model';
import { ValueType } from 'app/entities/enumerations/value-type.model';
import { v4 as uuidFunction } from 'uuid';

export interface IWorkoutStep {
  id?: number;
  uuid?: string;
  order?: number | null;
  executionTime?: number | null;
  type?: WorkoutStepType | null;
  status?: WorkoutStatus | null;
  exerciseUuid?: string;
  exerciseName?: string;
  exerciseValue?: number;
  exerciseValueType?: ValueType;
  workout?: IWorkout | null;
}

export class WorkoutStep implements IWorkoutStep {
  constructor(
    public id?: number,
    public uuid?: string,
    public order?: number | null,
    public executionTime?: number | null,
    public type?: WorkoutStepType | null,
    public status?: WorkoutStatus | null,
    public exerciseUuid?: string,
    public exerciseName?: string,
    public exerciseValue?: number,
    public exerciseValueType?: ValueType,
    public workout?: IWorkout | null
  ) {
    if (!this.uuid) {
      this.uuid = uuidFunction().toString();
    }
  }
}

export function getWorkoutStepIdentifier(workoutStep: IWorkoutStep): number | undefined {
  return workoutStep.id;
}
