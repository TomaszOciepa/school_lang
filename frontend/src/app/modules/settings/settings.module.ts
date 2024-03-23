import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SettingsRoutingModule } from './settings-routing.module';
import { SettingsComponent } from './settings.component';
import { ManageTeachersComponent } from './components/manage-teachers/manage-teachers.component';
import { ManageStudentsComponent } from './components/manage-students/manage-students.component';
import { TeachersTableComponent } from './components/manage-teachers/teachers-table/teachers-table.component';
import { SharedModule } from '../shared/shared.module';
import { StudentsTableComponent } from './components/manage-students/students-table/students-table.component';

@NgModule({
  declarations: [
    SettingsComponent,
    ManageTeachersComponent,
    ManageStudentsComponent,
    TeachersTableComponent,
    StudentsTableComponent,
  ],
  imports: [SharedModule, CommonModule, SettingsRoutingModule],
})
export class SettingsModule {}
