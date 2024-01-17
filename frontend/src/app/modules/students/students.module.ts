import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { StudentsRoutingModule } from './students-routing.module';
import { SharedModule } from '../shared/shared.module';
import { StudentsComponent } from './students.component';
import { StudentsTableComponent } from './components/students-table/students-table.component';

@NgModule({
  declarations: [StudentsComponent, StudentsTableComponent],
  imports: [SharedModule, StudentsRoutingModule],
})
export class StudentsModule {}
