import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StudentCoursesTableComponent } from './student-courses-table.component';

describe('StudentCoursesTableComponent', () => {
  let component: StudentCoursesTableComponent;
  let fixture: ComponentFixture<StudentCoursesTableComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [StudentCoursesTableComponent]
    });
    fixture = TestBed.createComponent(StudentCoursesTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
