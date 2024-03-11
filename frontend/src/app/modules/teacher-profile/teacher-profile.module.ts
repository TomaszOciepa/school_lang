import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TeacherProfileRoutingModule } from './teacher-profile-routing.module';
import { TeacherCoursesComponent } from './courses/teacher-courses/teacher-courses.component';
import { TeacherCoursesTableComponent } from './courses/teacher-courses-table/teacher-courses-table.component';
import { SharedModule } from '../shared/shared.module';
import { TeacherCourseComponent } from './courses/teacher-course/teacher-course.component';

@NgModule({
  declarations: [TeacherCoursesComponent, TeacherCoursesTableComponent, TeacherCourseComponent],
  imports: [SharedModule, CommonModule, TeacherProfileRoutingModule],
})
export class TeacherProfileModule {}
