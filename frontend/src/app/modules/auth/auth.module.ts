import { NgModule } from '@angular/core';

import { AuthRoutingModule } from './auth-routing.module';
import { LoginComponent } from './login/login.component';
import { LogoutComponent } from './logout/logout.component';
import { SharedModule } from '../shared/shared.module';

@NgModule({
  declarations: [LoginComponent, LogoutComponent],
  imports: [SharedModule, AuthRoutingModule],
})
export class AuthModule {}
