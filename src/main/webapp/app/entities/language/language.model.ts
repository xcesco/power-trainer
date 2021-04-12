import { ITranslation } from 'app/entities/translation/translation.model';

export interface ILanguage {
  id?: number;
  code?: string;
  name?: string;
  translations?: ITranslation[] | null;
}

export class Language implements ILanguage {
  constructor(public id?: number, public code?: string, public name?: string, public translations?: ITranslation[] | null) {}
}

export function getLanguageIdentifier(language: ILanguage): number | undefined {
  return language.id;
}
