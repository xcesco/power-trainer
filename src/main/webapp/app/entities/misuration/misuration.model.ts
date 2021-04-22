import * as dayjs from 'dayjs';
import { ICalendar } from 'app/entities/calendar/calendar.model';
import { IMisurationType } from 'app/entities/misuration-type/misuration-type.model';
import { v4 as uuidFunction } from 'uuid';

export interface IMisuration {
  id?: number;
  uuid?: string;
  date?: dayjs.Dayjs;
  value?: number;
  imageContentType?: string | null;
  image?: string | null;
  note?: string | null;
  calendar?: ICalendar | null;
  misurationType?: IMisurationType | null;
}

export class Misuration implements IMisuration {
  constructor(
    public id?: number,
    public uuid?: string,
    public date?: dayjs.Dayjs,
    public value?: number,
    public imageContentType?: string | null,
    public image?: string | null,
    public note?: string | null,
    public calendar?: ICalendar | null,
    public misurationType?: IMisurationType | null
  ) {
    if (!this.uuid) {
      this.uuid = uuidFunction().toString();
    }
  }
}

export function getMisurationIdentifier(misuration: IMisuration): number | undefined {
  return misuration.id;
}
