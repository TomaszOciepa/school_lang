import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TeacherCoursesTableComponent } from './teacher-courses-table.component';

describe('TeacherCoursesTableComponent', () => {
  let component: TeacherCoursesTableComponent;
  let fixture: ComponentFixture<TeacherCoursesTableComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TeacherCoursesTableComponent]
    });
    fixture = TestBed.createComponent(TeacherCoursesTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
