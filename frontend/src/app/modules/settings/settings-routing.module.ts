import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ManageTeachersComponent } from './components/manage-teachers/manage-teachers.component';
import { ManageStudentsComponent } from './components/manage-students/manage-students.component';

const routes: Routes = [
  { path: 'teachers', component: ManageTeachersComponent },
  { path: 'students', component: ManageStudentsComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class SettingsRoutingModule {}
