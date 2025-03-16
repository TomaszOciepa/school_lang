import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home.component';
import { CourseOfferingsComponent } from './course-offerings/course-offerings.component';
import { CoursesComponent } from './courses/courses.component';
import { CourseDetailsComponent } from './course-details/course-details.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'course-offering', component: CourseOfferingsComponent },
  { path: 'course-offering/courses/:language', component: CoursesComponent },
  { path: 'course/:id', component: CourseDetailsComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class HomeRoutingModule {}
