import { IExercise } from 'app/entities/exercise/exercise.model';
import { NoteType } from 'app/entities/enumerations/note-type.model';

export interface INote {
  id?: number;
  uuid?: string;
  type?: NoteType;
  url?: string | null;
  imageContentType?: string | null;
  image?: string | null;
  description?: string | null;
  exercise?: IExercise | null;
}

export class Note implements INote {
  constructor(
    public id?: number,
    public uuid?: string,
    public type?: NoteType,
    public url?: string | null,
    public imageContentType?: string | null,
    public image?: string | null,
    public description?: string | null,
    public exercise?: IExercise | null
  ) {}
}

export function getNoteIdentifier(note: INote): number | undefined {
  return note.id;
}