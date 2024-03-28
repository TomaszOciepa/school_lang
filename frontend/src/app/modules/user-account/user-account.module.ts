import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { UserAccountRoutingModule } from './user-account-routing.module';
import { UserAccountComponent } from './user-account.component';


@NgModule({
  declarations: [
    UserAccountComponent
  ],
  imports: [
    CommonModule,
    UserAccountRoutingModule
  ]
})
export class UserAccountModule { }
