import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RestoreStudentDialogComponent } from './restore-student-dialog.component';

describe('RestoreStudentDialogComponent', () => {
  let component: RestoreStudentDialogComponent;
  let fixture: ComponentFixture<RestoreStudentDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RestoreStudentDialogComponent]
    });
    fixture = TestBed.createComponent(RestoreStudentDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
