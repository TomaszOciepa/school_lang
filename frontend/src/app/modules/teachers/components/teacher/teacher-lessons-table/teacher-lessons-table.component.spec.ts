import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TeacherLessonsTableComponent } from './teacher-lessons-table.component';

describe('TeacherLessonsTableComponent', () => {
  let component: TeacherLessonsTableComponent;
  let fixture: ComponentFixture<TeacherLessonsTableComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TeacherLessonsTableComponent]
    });
    fixture = TestBed.createComponent(TeacherLessonsTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
