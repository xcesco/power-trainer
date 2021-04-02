export interface ICalendar {
  id?: number;
  uuid?: string;
  name?: string;
}

export class Calendar implements ICalendar {
  constructor(public id?: number, public uuid?: string, public name?: string) {}
}

export function getCalendarIdentifier(calendar: ICalendar): number | undefined {
  return calendar.id;
}
