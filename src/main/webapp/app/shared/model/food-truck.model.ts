import { IUserProfile } from 'app/shared/model/user-profile.model';
import { ICuisineType } from 'app/shared/model/cuisine-type.model';
import { ILocation } from 'app/shared/model/location.model';

export interface IFoodTruck {
  id?: number;
  name?: string | null;
  description?: string | null;
  rating?: number | null;
  profilePicture?: string | null;
  owner?: IUserProfile | null;
  cuisineType?: ICuisineType | null;
  location?: ILocation | null;
}

export const defaultValue: Readonly<IFoodTruck> = {};
