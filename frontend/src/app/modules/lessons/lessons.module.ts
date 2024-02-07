import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { LessonsRoutingModule } from './lessons-routing.module';
import { LessonsComponent } from './lessons.component';
import { LesonsTableComponent } from './components/lesons-table/lesons-table.component';
import { SharedModule } from '../shared/shared.module';
import { LessonComponent } from './components/lesson/lesson.component';
import { AttendanceListComponent } from './components/lesson/attendance-list/attendance-list.component';
import { LessonFormComponent } from './components/lesson-form/lesson-form.component';
import { EditLessonDialogComponent } from './components/lesson/edit-lesson-dialog/edit-lesson-dialog.component';
import { DeleteLessonDialogComponent } from './components/lesson/delete-lesson-dialog/delete-lesson-dialog.component';
import { EnrollLessonDialogComponent } from './components/lesson/enroll-lesson-dialog/enroll-lesson-dialog.component';
import { UnEnrollLessonDialogComponent } from './components/lesson/un-enroll-lesson-dialog/un-enroll-lesson-dialog.component';

@NgModule({
  declarations: [
    LessonsComponent,
    LesonsTableComponent,
    LessonComponent,
    AttendanceListComponent,
    LessonFormComponent,
    EditLessonDialogComponent,
    DeleteLessonDialogComponent,
    EnrollLessonDialogComponent,
    UnEnrollLessonDialogComponent,
  ],
  imports: [SharedModule, CommonModule, LessonsRoutingModule],
})
export class LessonsModule {}
