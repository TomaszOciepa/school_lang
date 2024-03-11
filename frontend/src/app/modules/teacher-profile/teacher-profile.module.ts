import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TeacherProfileRoutingModule } from './teacher-profile-routing.module';
import { TeacherCoursesComponent } from './teacher-courses/teacher-courses.component';
import { TeacherCoursesTableComponent } from './teacher-courses-table/teacher-courses-table.component';
import { SharedModule } from '../shared/shared.module';

@NgModule({
  declarations: [TeacherCoursesComponent, TeacherCoursesTableComponent],
  imports: [SharedModule, CommonModule, TeacherProfileRoutingModule],
})
export class TeacherProfileModule {}
