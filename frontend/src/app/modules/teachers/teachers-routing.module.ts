import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TeachersComponent } from './teachers.component';
import { TeacherDetailsComponent } from './components/teacher/teacher.component';
import { TeacherFormComponent } from './components/teacher-form/teacher-form.component';
import { TeacherSalaryDetailsComponent } from './components/teacher/teacher-salary-details/teacher-salary-details.component';

const routes: Routes = [
  { path: '', component: TeachersComponent },
  { path: 'dodaj', component: TeacherFormComponent },
  { path: ':id', component: TeacherDetailsComponent },
  { path: 'salary/:id', component: TeacherSalaryDetailsComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class TeachersRoutingModule {}
