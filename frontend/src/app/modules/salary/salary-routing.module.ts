import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SalaryComponent } from './salary.component';
import { UserSalaryDetailsComponent } from './user-salary-details/user-salary-details.component';

const routes: Routes = [
  { path: '', component: SalaryComponent },
  { path: 'details/:id', component: UserSalaryDetailsComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class SalaryRoutingModule {}
