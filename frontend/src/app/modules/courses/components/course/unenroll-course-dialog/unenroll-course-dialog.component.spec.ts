import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UnenrollCourseDialogComponent } from './unenroll-course-dialog.component';

describe('UnenrollCourseDialogComponent', () => {
  let component: UnenrollCourseDialogComponent;
  let fixture: ComponentFixture<UnenrollCourseDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UnenrollCourseDialogComponent]
    });
    fixture = TestBed.createComponent(UnenrollCourseDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
