import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TeachersRoutingModule } from './teachers-routing.module';
import { TeachersComponent } from './teachers.component';
import { TeachersTableComponent } from './components/teachers-table/teachers-table.component';
import { TeacherDetailsComponent } from './components/teacher-details/teacher-details.component';
import { TeacherFormComponent } from './components/teacher-form/teacher-form.component';
import { SharedModule } from '../shared/shared.module';

@NgModule({
  declarations: [
    TeachersComponent,
    TeachersTableComponent,
    TeacherDetailsComponent,
    TeacherFormComponent,
  ],
  imports: [SharedModule, TeachersRoutingModule],
})
export class TeachersModule {}
