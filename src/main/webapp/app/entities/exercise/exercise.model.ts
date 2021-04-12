import { IExerciseResource } from 'app/entities/exercise-resource/exercise-resource.model';
import { IMuscle } from 'app/entities/muscle/muscle.model';
import { IExerciseTool } from 'app/entities/exercise-tool/exercise-tool.model';
import { ValueType } from 'app/entities/enumerations/value-type.model';

export interface IExercise {
  id?: number;
  uuid?: string;
  imageContentType?: string | null;
  image?: string | null;
  name?: string;
  description?: string | null;
  valueType?: ValueType | null;
  owner?: string;
  exerciseResources?: IExerciseResource[] | null;
  muscles?: IMuscle[] | null;
  exerciseTools?: IExerciseTool[] | null;
}

export class Exercise implements IExercise {
  constructor(
    public id?: number,
    public uuid?: string,
    public imageContentType?: string | null,
    public image?: string | null,
    public name?: string,
    public description?: string | null,
    public valueType?: ValueType | null,
    public owner?: string,
    public exerciseResources?: IExerciseResource[] | null,
    public muscles?: IMuscle[] | null,
    public exerciseTools?: IExerciseTool[] | null
  ) {}
}

export function getExerciseIdentifier(exercise: IExercise): number | undefined {
  return exercise.id;
}
