import * as dayjs from 'dayjs';

export interface IExerciseValue {
  id?: number;
  uuid?: string;
  value?: number;
  date?: dayjs.Dayjs;
}

export class ExerciseValue implements IExerciseValue {
  constructor(public id?: number, public uuid?: string, public value?: number, public date?: dayjs.Dayjs) {}
}

export function getExerciseValueIdentifier(exerciseValue: IExerciseValue): number | undefined {
  return exerciseValue.id;
}
