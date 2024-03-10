import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CoursesComponent } from './courses.component';
import { CourseComponent } from './components/course/course.component';
import { CourseFormComponent } from './components/course-form/course-form.component';

const routes: Routes = [
  { path: '', component: CoursesComponent },
  { path: 'dodaj', component: CourseFormComponent },
  { path: ':id', component: CourseComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class CoursesRoutingModule {}
