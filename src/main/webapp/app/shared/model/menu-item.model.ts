import { IFoodTruck } from 'app/shared/model/food-truck.model';
import { IOrder } from 'app/shared/model/order.model';

export interface IMenuItem {
  id?: number;
  name?: string | null;
  description?: string | null;
  price?: number | null;
  truck?: IFoodTruck | null;
  orders?: IOrder[] | null;
}

export const defaultValue: Readonly<IMenuItem> = {};
