import { NgModule } from '@angular/core';

import { CoursesRoutingModule } from './courses-routing.module';
import { SharedModule } from '../shared/shared.module';
import { CoursesComponent } from './courses.component';
import { CoursesTableComponent } from './components/courses-table/courses-table.component';
import { CourseComponent } from './components/course/course.component';
import { CourseFormComponent } from './components/course-form/course-form.component';
import { EditCourseDialogComponent } from './components/course/edit-course-dialog/edit-course-dialog.component';
import { DeleteCourseDialogComponent } from './components/course/delete-course-dialog/delete-course-dialog.component';

@NgModule({
  declarations: [CoursesComponent, CoursesTableComponent, CourseComponent, CourseFormComponent, EditCourseDialogComponent, DeleteCourseDialogComponent],
  imports: [SharedModule, CoursesRoutingModule],
})
export class CoursesModule {}
