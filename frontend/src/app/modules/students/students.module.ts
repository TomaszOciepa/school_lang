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
import { LessonsModule } from '../lessons/lessons.module';
import { StudentCoursesTableComponent } from './components/student/student-courses-table/student-courses-table.component';
import { StudentLessonsTableComponent } from './components/student/student-lessons-table/student-lessons-table.component';
import { DropStudentDialogComponent } from './components/student/drop-student-dialog/drop-student-dialog.component';
import { RestoreStudentAccountDialogComponent } from './components/student/restore-student-account-dialog/restore-student-account-dialog.component';
import { StudentPaymentsTableComponent } from './components/student/student-payments-table/student-payments-table.component';

@NgModule({
  declarations: [
    StudentsComponent,
    StudentsTableComponent,
    StudentComponent,
    EditStudentDialogComponent,
    StudentFormComponent,
    DeleteStudentDialogComponent,
    StudentCoursesTableComponent,
    StudentLessonsTableComponent,
    DropStudentDialogComponent,
    RestoreStudentAccountDialogComponent,
    StudentPaymentsTableComponent,
  ],
  imports: [SharedModule, StudentsRoutingModule, LessonsModule],
})
export class StudentsModule {}
