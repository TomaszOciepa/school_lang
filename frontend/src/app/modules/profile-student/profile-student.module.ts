import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ProfileStudentRoutingModule } from './profile-student-routing.module';
import { ProfileStudentComponent } from './profile-student.component';
import { CoursesComponent } from './components/courses/courses.component';
import { LessonsComponent } from './components/lessons/lessons.component';
import { MyAccountComponent } from './components/my-account/my-account.component';
import { SharedModule } from '../shared/shared.module';
import { CoursesTableComponent } from './components/courses/courses-table/courses-table.component';
import { CourseComponent } from './components/courses/course/course.component';

@NgModule({
  declarations: [
    ProfileStudentComponent,
    CoursesComponent,
    LessonsComponent,
    MyAccountComponent,
    CoursesTableComponent,
    CourseComponent,
  ],
  imports: [SharedModule, CommonModule, ProfileStudentRoutingModule],
})
export class ProfileStudentModule {}
