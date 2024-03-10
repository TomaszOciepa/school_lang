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
      import('./modules/admin/courses/courses.module').then(
        (m) => m.CoursesModule
      ),
    canActivate: [AuthGuard],
    data: {
      roles: ['admin'],
    },
    title: 'Kursy',
  },
  {
    path: 'students',
    loadChildren: () =>
      import('./modules/admin/students/students.module').then(
        (m) => m.StudentsModule
      ),
    canActivate: [AuthGuard],
    data: {
      roles: ['admin'],
    },
    title: 'Uczniowie',
  },
  {
    path: 'teachers',
    loadChildren: () =>
      import('./modules/admin/teachers/teachers.module').then(
        (m) => m.TeachersModule
      ),
    canActivate: [AuthGuard],
    data: {
      roles: ['admin'],
    },
    title: 'nauczyciele',
  },
  {
    path: 'lessons',
    loadChildren: () =>
      import('./modules/admin/lessons/lessons.module').then(
        (m) => m.LessonsModule
      ),
    canActivate: [AuthGuard],
    data: {
      roles: ['admin'],
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
