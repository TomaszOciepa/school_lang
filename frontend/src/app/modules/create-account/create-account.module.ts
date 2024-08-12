import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CreateAccountRoutingModule } from './create-account-routing.module';
import { RegisterFormComponent } from './register-form/register-form.component';
import { CreateUserAccountComponent } from './create-user-account/create-user-account.component';
import { SharedModule } from '../shared/shared.module';

@NgModule({
  declarations: [RegisterFormComponent, CreateUserAccountComponent],
  imports: [CommonModule, CreateAccountRoutingModule, SharedModule],
})
export class CreateAccountModule {}
