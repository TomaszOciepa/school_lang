import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { UserAccountRoutingModule } from './user-account-routing.module';
import { UserAccountComponent } from './user-account.component';
import { SharedModule } from '../shared/shared.module';

@NgModule({
  declarations: [UserAccountComponent],
  imports: [CommonModule, SharedModule, UserAccountRoutingModule],
})
export class UserAccountModule {}
