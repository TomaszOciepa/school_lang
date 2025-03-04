import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SalaryRoutingModule } from './salary-routing.module';
import { SalaryComponent } from './salary.component';
import { UserSalaryComponent } from './user-salary/user-salary.component';
import { UserSalaryDetailsComponent } from './user-salary-details/user-salary-details.component';
import { SharedModule } from '../shared/shared.module';

@NgModule({
  declarations: [
    SalaryComponent,
    UserSalaryComponent,
    UserSalaryDetailsComponent,
  ],
  imports: [SharedModule, CommonModule, SalaryRoutingModule],
})
export class SalaryModule {}
