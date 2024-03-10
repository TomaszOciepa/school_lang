import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UnEnrollLessonDialogComponent } from './un-enroll-lesson-dialog.component';

describe('UnEnrollLessonDialogComponent', () => {
  let component: UnEnrollLessonDialogComponent;
  let fixture: ComponentFixture<UnEnrollLessonDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UnEnrollLessonDialogComponent]
    });
    fixture = TestBed.createComponent(UnEnrollLessonDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
