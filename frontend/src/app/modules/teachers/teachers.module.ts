import { NgModule } from '@angular/core';

import { TeachersRoutingModule } from './teachers-routing.module';
import { TeachersComponent } from './teachers.component';
import { TeachersTableComponent } from './components/teachers-table/teachers-table.component';
import { TeacherDetailsComponent } from './components/teacher/teacher.component';
import { TeacherFormComponent } from './components/teacher-form/teacher-form.component';
import { SharedModule } from '../shared/shared.module';
import { DeleteTeacherDialogComponent } from './components/teacher/delete-teacher-dialog/delete-teacher-dialog.component';
import { EditTeacherDialogComponent } from './components/teacher/edit-teacher-dialog/edit-teacher-dialog.component';
import { LessonsModule } from '../lessons/lessons.module';
import { TeacherCoursesTableComponent } from './components/teacher/teacher-courses-table/teacher-courses-table.component';
import { TeacherLessonsTableComponent } from './components/teacher/teacher-lessons-table/teacher-lessons-table.component';
import { DropTeacherDialogComponent } from './components/teacher/drop-teacher-dialog/drop-teacher-dialog.component';
import { RestoreTeacherAccountDialogComponent } from './components/teacher/restore-teacher-account-dialog/restore-teacher-account-dialog.component';
import { TeacherSalaryComponent } from './components/teacher/teacher-salary/teacher-salary.component';

@NgModule({
  declarations: [
    TeachersComponent,
    TeachersTableComponent,
    TeacherDetailsComponent,
    TeacherFormComponent,
    DeleteTeacherDialogComponent,
    EditTeacherDialogComponent,
    TeacherCoursesTableComponent,
    TeacherLessonsTableComponent,
    DropTeacherDialogComponent,
    RestoreTeacherAccountDialogComponent,
    TeacherSalaryComponent,
  ],
  imports: [SharedModule, TeachersRoutingModule, LessonsModule],
})
export class TeachersModule {}
