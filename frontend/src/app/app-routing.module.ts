import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { AuthGuard } from './modules/core/guards/authguard.guard';

const routes: Routes = [
  {
    path: '',
    loadChildren: () =>
      import('./modules/home/home.module').then((m) => m.HomeModule),
    title: 'Lang School',
  },
  {
    path: 'courses',
    loadChildren: () =>
      import('./modules/courses/courses.module').then((m) => m.CoursesModule),
    canActivate: [AuthGuard],
    data: {
      roles: ['admin', 'user'],
    },
    title: 'Kursy',
  },
  {
    path: 'students',
    loadChildren: () =>
      import('./modules/students/students.module').then(
        (m) => m.StudentsModule
      ),
    canActivate: [AuthGuard],
    data: {
      roles: ['admin', 'user'],
    },
    title: 'Uczniowie',
  },
  {
    path: 'teachers',
    loadChildren: () =>
      import('./modules/teachers/teachers.module').then(
        (m) => m.TeachersModule
      ),
    canActivate: [AuthGuard],
    data: {
      roles: ['admin', 'user'],
    },
    title: 'nauczyciele',
  },
  {
    path: 'lessons',
    loadChildren: () =>
      import('./modules/lessons/lessons.module').then((m) => m.LessonsModule),
    canActivate: [AuthGuard],
    data: {
      roles: ['admin', 'user'],
    },
    title: 'lekcje',
  },
  { path: '**', component: PageNotFoundComponent, title: 'Page not found 404' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],

  exports: [RouterModule],
})
export class AppRoutingModule {}
