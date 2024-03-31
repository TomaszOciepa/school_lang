import { NgModule } from '@angular/core';

import { HomeRoutingModule } from './home-routing.module';
import { HomeComponent } from './home.component';
import { SharedModule } from '../shared/shared.module';
import { CourseOfferingsComponent } from './course-offerings/course-offerings.component';
import { CoursesComponent } from './courses/courses.component';
import { CourseDetailsComponent } from './course-details/course-details.component';
import { LessonsTableComponent } from './lessons-table/lessons-table.component';

@NgModule({
  declarations: [HomeComponent, CourseOfferingsComponent, CoursesComponent, CourseDetailsComponent, LessonsTableComponent],
  imports: [SharedModule, HomeRoutingModule],
})
export class HomeModule {}
