import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EnrollLessonDialogComponent } from './enroll-lesson-dialog.component';

describe('EnrollLessonDialogComponent', () => {
  let component: EnrollLessonDialogComponent;
  let fixture: ComponentFixture<EnrollLessonDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EnrollLessonDialogComponent]
    });
    fixture = TestBed.createComponent(EnrollLessonDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
