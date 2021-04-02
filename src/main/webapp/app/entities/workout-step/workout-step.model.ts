import { ValueType } from 'app/entities/enumerations/value-type.model';
import { WorkoutStepType } from 'app/entities/enumerations/workout-step-type.model';
import { WorkoutStatus } from 'app/entities/enumerations/workout-status.model';

export interface IWorkoutStep {
  id?: number;
  uuid?: string;
  order?: number | null;
  value?: number | null;
  valueType?: ValueType | null;
  executionTime?: number | null;
  type?: WorkoutStepType | null;
  status?: WorkoutStatus | null;
}

export class WorkoutStep implements IWorkoutStep {
  constructor(
    public id?: number,
    public uuid?: string,
    public order?: number | null,
    public value?: number | null,
    public valueType?: ValueType | null,
    public executionTime?: number | null,
    public type?: WorkoutStepType | null,
    public status?: WorkoutStatus | null
  ) {}
}

export function getWorkoutStepIdentifier(workoutStep: IWorkoutStep): number | undefined {
  return workoutStep.id;
}
