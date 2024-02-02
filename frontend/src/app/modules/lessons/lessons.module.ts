import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { LessonsRoutingModule } from './lessons-routing.module';
import { LessonsComponent } from './lessons.component';
import { LesonsTableComponent } from './components/lesons-table/lesons-table.component';
import { SharedModule } from '../shared/shared.module';

@NgModule({
  declarations: [LessonsComponent, LesonsTableComponent],
  imports: [SharedModule, CommonModule, LessonsRoutingModule],
})
export class LessonsModule {}
