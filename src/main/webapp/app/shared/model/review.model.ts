import dayjs from 'dayjs';
import { IUserProfile } from 'app/shared/model/user-profile.model';

export interface IReview {
  id?: number;
  rating?: number | null;
  comment?: string | null;
  date?: dayjs.Dayjs | null;
  user?: IUserProfile | null;
}

export const defaultValue: Readonly<IReview> = {};
