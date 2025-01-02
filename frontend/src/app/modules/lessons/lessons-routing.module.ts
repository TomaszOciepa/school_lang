import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LessonsComponent } from './lessons.component';
import { LessonComponent } from './components/lesson/lesson.component';
import { LessonFormComponent } from './components/lesson-form/lesson-form.component';

const routes: Routes = [
  { path: '', component: LessonsComponent },
  { path: 'dodaj', component: LessonFormComponent },
  { path: ':id', component: LessonComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class LessonsRoutingModule {}
