import { IExercise } from 'app/entities/exercise/exercise.model';
import { v4 as uuidFunction } from 'uuid';

export interface IExerciseTool {
  id?: number;
  uuid?: string;
  imageContentType?: string | null;
  image?: string | null;
  name?: string;
  description?: string | null;
  exercises?: IExercise[] | null;
}

export class ExerciseTool implements IExerciseTool {
  constructor(
    public id?: number,
    public uuid?: string,
    public imageContentType?: string | null,
    public image?: string | null,
    public name?: string,
    public description?: string | null,
    public exercises?: IExercise[] | null
  ) {
    if (!this.uuid) {
      this.uuid = uuidFunction().toString();
    }
  }
}

export function getExerciseToolIdentifier(exerciseTool: IExerciseTool): number | undefined {
  return exerciseTool.id;
}
