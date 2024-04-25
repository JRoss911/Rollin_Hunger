import dayjs from 'dayjs';
import { ILocation } from 'app/shared/model/location.model';

export interface IEvent {
  id?: number;
  name?: string | null;
  date?: dayjs.Dayjs | null;
  description?: string | null;
  location?: ILocation | null;
}

export const defaultValue: Readonly<IEvent> = {};
