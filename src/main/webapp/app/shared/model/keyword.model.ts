import { IEntry } from 'app/shared/model/entry.model';

export interface IKeyword {
  id?: number;
  name?: string;
  entries?: IEntry[];
}

export class Keyword implements IKeyword {
  constructor(public id?: number, public name?: string, public entries?: IEntry[]) {}
}
