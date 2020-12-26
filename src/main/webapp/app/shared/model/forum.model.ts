import { IUser } from 'app/core/user/user.model';

export interface IForum {
  id?: number;
  name?: string;
  handle?: string;
  user?: IUser;
}

export class Forum implements IForum {
  constructor(public id?: number, public name?: string, public handle?: string, public user?: IUser) {}
}
