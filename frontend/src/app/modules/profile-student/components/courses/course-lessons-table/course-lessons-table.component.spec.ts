import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CourseLessonsTableComponent } from './course-lessons-table.component';

describe('CourseLessonsTableComponent', () => {
  let component: CourseLessonsTableComponent;
  let fixture: ComponentFixture<CourseLessonsTableComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CourseLessonsTableComponent]
    });
    fixture = TestBed.createComponent(CourseLessonsTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
