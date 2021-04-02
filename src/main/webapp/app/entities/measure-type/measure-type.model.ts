export interface IMeasureType {
  id?: number;
  uuid?: string;
  name?: string;
  imageContentType?: string | null;
  image?: string | null;
  note?: string | null;
}

export class MeasureType implements IMeasureType {
  constructor(
    public id?: number,
    public uuid?: string,
    public name?: string,
    public imageContentType?: string | null,
    public image?: string | null,
    public note?: string | null
  ) {}
}

export function getMeasureTypeIdentifier(measureType: IMeasureType): number | undefined {
  return measureType.id;
}
