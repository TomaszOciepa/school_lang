import { FormControl } from '@angular/forms';

export interface UserResponse {
  id: number;
  firstName?: string;
  lastName?: string;
  email?: string;
  status: string;
}

export type PostUser = Omit<UserResponse, 'id' | 'status'>;

export class User implements UserResponse {
  constructor(
    public id: number,
    public firstName: string,
    public lastName: string,
    public email: string,
    public status: string
  ) {}
}

export interface PostUserForm {
  firstName: FormControl<string>;
  lastName: FormControl<string>;
  email: FormControl<string>;
}
