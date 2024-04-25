export interface ILocation {
  id?: number;
  address?: string | null;
  latitude?: number | null;
  longitude?: number | null;
}

export const defaultValue: Readonly<ILocation> = {};
