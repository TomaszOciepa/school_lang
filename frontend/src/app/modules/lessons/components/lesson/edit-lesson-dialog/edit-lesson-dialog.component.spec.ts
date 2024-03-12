import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditLessonDialogComponent } from './edit-lesson-dialog.component';

describe('EditLessonDialogComponent', () => {
  let component: EditLessonDialogComponent;
  let fixture: ComponentFixture<EditLessonDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EditLessonDialogComponent]
    });
    fixture = TestBed.createComponent(EditLessonDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
