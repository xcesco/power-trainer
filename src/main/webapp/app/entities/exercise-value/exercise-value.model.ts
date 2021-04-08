import * as dayjs from 'dayjs';
import { ICalendar } from 'app/entities/calendar/calendar.model';
import { ValueType } from 'app/entities/enumerations/value-type.model';

export interface IExerciseValue {
  id?: number;
  uuid?: string;
  date?: dayjs.Dayjs;
  exerciseUuid?: string;
  exerciseName?: string;
  exerciseValue?: number;
  exerciseValueType?: ValueType;
  calendar?: ICalendar | null;
}

export class ExerciseValue implements IExerciseValue {
  constructor(
    public id?: number,
    public uuid?: string,
    public date?: dayjs.Dayjs,
    public exerciseUuid?: string,
    public exerciseName?: string,
    public exerciseValue?: number,
    public exerciseValueType?: ValueType,
    public calendar?: ICalendar | null
  ) {}
}

export function getExerciseValueIdentifier(exerciseValue: IExerciseValue): number | undefined {
  return exerciseValue.id;
}
