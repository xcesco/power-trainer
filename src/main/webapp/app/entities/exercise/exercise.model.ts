import { IExerciseResource } from 'app/entities/exercise-resource/exercise-resource.model';
import { IMuscle } from 'app/entities/muscle/muscle.model';
import { IExerciseTool } from 'app/entities/exercise-tool/exercise-tool.model';
import { ValueType } from 'app/entities/enumerations/value-type.model';
import { v4 as uuidFunction } from 'uuid';

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
  ) {
    if (!this.uuid) {
      this.uuid = uuidFunction().toString();
    }
  }
}

export function getExerciseIdentifier(exercise: IExercise): number | undefined {
  return exercise.id;
}
