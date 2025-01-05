import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CourseEditFormComponent } from './course-edit-form.component';

describe('CourseEditFormComponent', () => {
  let component: CourseEditFormComponent;
  let fixture: ComponentFixture<CourseEditFormComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CourseEditFormComponent]
    });
    fixture = TestBed.createComponent(CourseEditFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
