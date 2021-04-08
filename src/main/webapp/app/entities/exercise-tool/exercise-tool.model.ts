import { IExercise } from 'app/entities/exercise/exercise.model';

export interface IExerciseTool {
  id?: number;
  uuid?: string;
  imageContentType?: string | null;
  image?: string | null;
  name?: string;
  description?: string | null;
  exercise?: IExercise | null;
}

export class ExerciseTool implements IExerciseTool {
  constructor(
    public id?: number,
    public uuid?: string,
    public imageContentType?: string | null,
    public image?: string | null,
    public name?: string,
    public description?: string | null,
    public exercise?: IExercise | null
  ) {}
}

export function getExerciseToolIdentifier(exerciseTool: IExerciseTool): number | undefined {
  return exerciseTool.id;
}
