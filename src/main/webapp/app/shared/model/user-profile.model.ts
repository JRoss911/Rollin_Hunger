export interface IUserProfile {
  id?: number;
  email?: string | null;
  points?: number | null;
}

export const defaultValue: Readonly<IUserProfile> = {};
