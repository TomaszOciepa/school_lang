import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CoursesComponent } from '../profile-student/components/courses/courses.component';
import { LessonsComponent } from '../profile-student/components/lessons/lessons.component';
import { MyAccountComponent } from '../profile-student/components/my-account/my-account.component';
import { CourseComponent } from '../profile-student/components/courses/course/course.component';
import { LessonComponent } from '../profile-student/components/lessons/lesson/lesson.component';

const routes: Routes = [
  { path: 'my-courses', component: CoursesComponent },
  { path: 'my-lessons', component: LessonsComponent },
  { path: 'my-account', component: MyAccountComponent },
  { path: 'course/:id', component: CourseComponent },
  { path: 'lesson/:id', component: LessonComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ProfileStudentRoutingModule {}
