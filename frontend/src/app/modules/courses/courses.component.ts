import { Component } from '@angular/core';
import { LoadUserProfileService } from '../core/services/load-user-profile.service';
import { KeycloakProfile } from 'keycloak-js';
import { TeacherService } from '../core/services/teacher.service';
import { async } from '@angular/core/testing';

@Component({
  selector: 'app-courses',
  templateUrl: './courses.component.html',
  styleUrls: ['./courses.component.css'],
})
export class CoursesComponent {}
