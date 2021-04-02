import * as dayjs from 'dayjs';

export interface IMeasureValue {
  id?: number;
  uuid?: string;
  date?: dayjs.Dayjs;
  value?: number;
  note?: string | null;
}

export class MeasureValue implements IMeasureValue {
  constructor(public id?: number, public uuid?: string, public date?: dayjs.Dayjs, public value?: number, public note?: string | null) {}
}

export function getMeasureValueIdentifier(measureValue: IMeasureValue): number | undefined {
  return measureValue.id;
}
