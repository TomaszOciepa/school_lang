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
import { LessonsTableComponent } from './components/lessons/lessons-table/lessons-table.component';
import { LessonComponent } from './components/lessons/lesson/lesson.component';
import { CourseLessonsTableComponent } from './components/courses/course-lessons-table/course-lessons-table.component';
import { EnrollToCourseComponent } from './components/enroll-to-course/enroll-to-course.component';
import { PaymentsComponent } from './components/payments/payments.component';
import { PaymentsTableComponent } from './components/payments/payments-table/payments-table.component';

@NgModule({
  declarations: [
    ProfileStudentComponent,
    CoursesComponent,
    LessonsComponent,
    MyAccountComponent,
    CoursesTableComponent,
    CourseComponent,
    LessonsTableComponent,
    LessonComponent,
    CourseLessonsTableComponent,
    EnrollToCourseComponent,
    PaymentsComponent,
    PaymentsTableComponent,
  ],
  imports: [SharedModule, CommonModule, ProfileStudentRoutingModule],
})
export class ProfileStudentModule {}
