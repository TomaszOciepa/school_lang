import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DropTeacherDialogComponent } from './drop-teacher-dialog.component';

describe('DropTeacherDialogComponent', () => {
  let component: DropTeacherDialogComponent;
  let fixture: ComponentFixture<DropTeacherDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DropTeacherDialogComponent]
    });
    fixture = TestBed.createComponent(DropTeacherDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
