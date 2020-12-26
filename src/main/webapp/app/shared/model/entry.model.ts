import { Moment } from 'moment';
import { IForum } from 'app/shared/model/forum.model';
import { IKeyword } from 'app/shared/model/keyword.model';

export interface IEntry {
  id?: number;
  title?: string;
  content?: any;
  date?: Moment;
  forum?: IForum;
  keywords?: IKeyword[];
}

export class Entry implements IEntry {
  constructor(
    public id?: number,
    public title?: string,
    public content?: any,
    public date?: Moment,
    public forum?: IForum,
    public keywords?: IKeyword[]
  ) {}
}
