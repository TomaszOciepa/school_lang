import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { StudentsRoutingModule } from './students-routing.module';
import { SharedModule } from '../shared/shared.module';
import { StudentsComponent } from './students.component';
import { StudentsTableComponent } from './components/students-table/students-table.component';
import { EditStudentDialogComponent } from './components/student/edit-student-dialog/edit-student-dialog.component';
import { StudentComponent } from './components/student/student.component';
import { StudentFormComponent } from './components/student-form/student-form.component';
import { DeleteStudentDialogComponent } from './components/student/delete-student-dialog/delete-student-dialog.component';
import { CoursesModule } from '../courses/courses.module';
import { LessonsModule } from '../lessons/lessons.module';

@NgModule({
  declarations: [
    StudentsComponent,
    StudentsTableComponent,
    StudentComponent,
    EditStudentDialogComponent,
    StudentFormComponent,
    DeleteStudentDialogComponent,
  ],
  imports: [SharedModule, StudentsRoutingModule, CoursesModule, LessonsModule],
})
export class StudentsModule {}
