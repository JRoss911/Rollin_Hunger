import dayjs from 'dayjs';
import { IMenuItem } from 'app/shared/model/menu-item.model';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { IFoodTruck } from 'app/shared/model/food-truck.model';

export interface IOrder {
  id?: number;
  quantity?: number | null;
  status?: string | null;
  timestamp?: dayjs.Dayjs | null;
  menuItems?: IMenuItem[] | null;
  user?: IUserProfile | null;
  foodTruck?: IFoodTruck | null;
}

export const defaultValue: Readonly<IOrder> = {};
