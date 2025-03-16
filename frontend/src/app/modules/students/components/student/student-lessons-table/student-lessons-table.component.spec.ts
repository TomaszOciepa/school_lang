import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StudentLessonsTableComponent } from './student-lessons-table.component';

describe('StudentLessonsTableComponent', () => {
  let component: StudentLessonsTableComponent;
  let fixture: ComponentFixture<StudentLessonsTableComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [StudentLessonsTableComponent]
    });
    fixture = TestBed.createComponent(StudentLessonsTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
