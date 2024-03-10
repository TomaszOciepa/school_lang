import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { HeaderComponent } from './components/header/header.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { SharedModule } from '../shared/shared.module';
import { AdminComponent } from './components/header/admin/admin.component';
import { TeacherComponent } from './components/header/teacher/teacher.component';
import { StudentComponent } from './components/header/student/student.component';

@NgModule({
  declarations: [HeaderComponent, AdminComponent, TeacherComponent, StudentComponent],
  imports: [SharedModule, RouterModule, BrowserAnimationsModule],
  exports: [HeaderComponent],
})
export class CoreModule {}
