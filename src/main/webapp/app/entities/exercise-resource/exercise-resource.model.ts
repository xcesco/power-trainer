import { IExercise } from 'app/entities/exercise/exercise.model';
import { ExerciseResourceType } from 'app/entities/enumerations/exercise-resource-type.model';
import { v4 as uuidFunction } from 'uuid';

export interface IExerciseResource {
  id?: number;
  uuid?: string;
  order?: number | null;
  type?: ExerciseResourceType;
  url?: string | null;
  imageContentType?: string | null;
  image?: string | null;
  description?: string | null;
  exercise?: IExercise | null;
}

export class ExerciseResource implements IExerciseResource {
  constructor(
    public id?: number,
    public uuid?: string,
    public order?: number | null,
    public type?: ExerciseResourceType,
    public url?: string | null,
    public imageContentType?: string | null,
    public image?: string | null,
    public description?: string | null,
    public exercise?: IExercise | null
  ) {
    if (!this.uuid) {
      this.uuid = uuidFunction().toString();
    }
  }
}

export function getExerciseResourceIdentifier(exerciseResource: IExerciseResource): number | undefined {
  return exerciseResource.id;
}
