import { ValueType } from 'app/entities/enumerations/value-type.model';

export interface IExercise {
  id?: number;
  uuid?: string;
  imageContentType?: string | null;
  image?: string | null;
  name?: string;
  description?: string | null;
  valueType?: ValueType | null;
}

export class Exercise implements IExercise {
  constructor(
    public id?: number,
    public uuid?: string,
    public imageContentType?: string | null,
    public image?: string | null,
    public name?: string,
    public description?: string | null,
    public valueType?: ValueType | null
  ) {}
}

export function getExerciseIdentifier(exercise: IExercise): number | undefined {
  return exercise.id;
}
