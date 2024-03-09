import { NgModule } from '@angular/core';

import { TeachersRoutingModule } from './teachers-routing.module';
import { TeachersComponent } from './teachers.component';
import { TeachersTableComponent } from './components/teachers-table/teachers-table.component';
import { TeacherDetailsComponent } from './components/teacher/teacher.component';
import { TeacherFormComponent } from './components/teacher-form/teacher-form.component';
import { SharedModule } from '../shared/shared.module';
import { DeleteTeacherDialogComponent } from './components/teacher/delete-teacher-dialog/delete-teacher-dialog.component';
import { EditTeacherDialogComponent } from './components/teacher/edit-teacher-dialog/edit-teacher-dialog.component';
import { CoursesModule } from '../courses/courses.module';
import { LessonsModule } from '../lessons/lessons.module';

@NgModule({
  declarations: [
    TeachersComponent,
    TeachersTableComponent,
    TeacherDetailsComponent,
    TeacherFormComponent,
    DeleteTeacherDialogComponent,
    EditTeacherDialogComponent,
  ],
  imports: [SharedModule, TeachersRoutingModule, CoursesModule, LessonsModule],
})
export class TeachersModule {}
