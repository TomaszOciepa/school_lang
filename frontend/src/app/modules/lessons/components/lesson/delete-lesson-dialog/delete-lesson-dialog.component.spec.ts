import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeleteLessonDialogComponent } from './delete-lesson-dialog.component';

describe('DeleteLessonDialogComponent', () => {
  let component: DeleteLessonDialogComponent;
  let fixture: ComponentFixture<DeleteLessonDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DeleteLessonDialogComponent]
    });
    fixture = TestBed.createComponent(DeleteLessonDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
