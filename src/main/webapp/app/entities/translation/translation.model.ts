import { ILanguage } from 'app/entities/language/language.model';

export interface ITranslation {
  id?: number;
  entityType?: string;
  entityUuid?: string;
  value?: string;
  language?: ILanguage | null;
}

export class Translation implements ITranslation {
  constructor(
    public id?: number,
    public entityType?: string,
    public entityUuid?: string,
    public value?: string,
    public language?: ILanguage | null
  ) {}
}

export function getTranslationIdentifier(translation: ITranslation): number | undefined {
  return translation.id;
}
