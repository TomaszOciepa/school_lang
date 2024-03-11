import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TeacherProfileModule } from './teacher-profile.module';
import { TeacherCoursesComponent } from './teacher-courses/teacher-courses.component';

const routes: Routes = [
  { path: 'courses', component: TeacherCoursesComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class TeacherProfileRoutingModule {}
