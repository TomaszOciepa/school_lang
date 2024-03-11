import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TeacherCoursesComponent } from './teacher-courses.component';

describe('TeacherCoursesComponent', () => {
  let component: TeacherCoursesComponent;
  let fixture: ComponentFixture<TeacherCoursesComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TeacherCoursesComponent]
    });
    fixture = TestBed.createComponent(TeacherCoursesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
