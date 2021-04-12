import { IMisuration } from 'app/entities/misuration/misuration.model';

export interface IMisurationType {
  id?: number;
  uuid?: string;
  name?: string;
  imageContentType?: string | null;
  image?: string | null;
  description?: string | null;
  misurations?: IMisuration[] | null;
}

export class MisurationType implements IMisurationType {
  constructor(
    public id?: number,
    public uuid?: string,
    public name?: string,
    public imageContentType?: string | null,
    public image?: string | null,
    public description?: string | null,
    public misurations?: IMisuration[] | null
  ) {}
}

export function getMisurationTypeIdentifier(misurationType: IMisurationType): number | undefined {
  return misurationType.id;
}
