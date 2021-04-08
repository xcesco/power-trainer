import { IExerciseTool } from 'app/entities/exercise-tool/exercise-tool.model';
import { INote } from 'app/entities/note/note.model';
import { IMuscle } from 'app/entities/muscle/muscle.model';
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
  exerciseTools?: IExerciseTool[] | null;
  notes?: INote[] | null;
  muscles?: IMuscle[] | null;
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
    public exerciseTools?: IExerciseTool[] | null,
    public notes?: INote[] | null,
    public muscles?: IMuscle[] | null
  ) {}
}

export function getExerciseIdentifier(exercise: IExercise): number | undefined {
  return exercise.id;
}