import { IExercise } from 'app/entities/exercise/exercise.model';
import { v4 as uuidFunction } from 'uuid';

export interface IMuscle {
  id?: number;
  uuid?: string;
  name?: string;
  imageContentType?: string | null;
  image?: string | null;
  note?: string | null;
  exercises?: IExercise[] | null;
}

export class Muscle implements IMuscle {
  constructor(
    public id?: number,
    public uuid?: string,
    public name?: string,
    public imageContentType?: string | null,
    public image?: string | null,
    public note?: string | null,
    public exercises?: IExercise[] | null
  ) {
    if (!this.uuid) {
      this.uuid = uuidFunction().toString();
    }
  }
}

export function getMuscleIdentifier(muscle: IMuscle): number | undefined {
  return muscle.id;
}
