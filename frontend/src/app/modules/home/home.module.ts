import { NgModule } from '@angular/core';

import { HomeRoutingModule } from './home-routing.module';
import { HomeComponent } from './home.component';
import { SharedModule } from '../shared/shared.module';
import { CourseOfferingsComponent } from './course-offerings/course-offerings.component';
import { CoursesComponent } from './courses/courses.component';

@NgModule({
  declarations: [HomeComponent, CourseOfferingsComponent, CoursesComponent],
  imports: [SharedModule, HomeRoutingModule],
})
export class HomeModule {}
