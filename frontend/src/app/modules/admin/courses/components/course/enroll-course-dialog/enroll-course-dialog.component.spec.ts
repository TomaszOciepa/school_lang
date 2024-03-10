import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EnrollCourseDialogComponent } from './enroll-course-dialog.component';

describe('EnrollCourseDialogComponent', () => {
  let component: EnrollCourseDialogComponent;
  let fixture: ComponentFixture<EnrollCourseDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EnrollCourseDialogComponent]
    });
    fixture = TestBed.createComponent(EnrollCourseDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
