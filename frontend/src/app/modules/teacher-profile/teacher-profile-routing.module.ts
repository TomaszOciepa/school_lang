import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TeacherCoursesComponent } from './courses/teacher-courses/teacher-courses.component';
import { TeacherCourseComponent } from './courses/teacher-course/teacher-course.component';

const routes: Routes = [
  { path: 'courses', component: TeacherCoursesComponent },
  { path: 'course/:id', component: TeacherCourseComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class TeacherProfileRoutingModule {}
