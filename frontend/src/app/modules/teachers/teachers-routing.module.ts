import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TeachersComponent } from './teachers.component';
import { TeacherDetailsComponent } from './components/teacher-details/teacher-details.component';

const routes: Routes = [
  { path: '', component: TeachersComponent },
  { path: ':id', component: TeacherDetailsComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class TeachersRoutingModule {}
