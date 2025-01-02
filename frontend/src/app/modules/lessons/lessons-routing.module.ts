import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LessonsComponent } from './lessons.component';
import { LessonComponent } from './components/lesson/lesson.component';
import { LessonFormComponent } from './components/lesson-form/lesson-form.component';
import { LessonsGeneratedFormComponent } from './components/lessons-generated-form/lessons-generated-form.component';

const routes: Routes = [
  { path: '', component: LessonsComponent },
  { path: 'dodaj', component: LessonFormComponent },
  { path: 'generated', component: LessonsGeneratedFormComponent },
  { path: ':id', component: LessonComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class LessonsRoutingModule {}
