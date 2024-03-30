import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RestoreStudentAccountDialogComponent } from './restore-student-account-dialog.component';

describe('RestoreStudentAccountDialogComponent', () => {
  let component: RestoreStudentAccountDialogComponent;
  let fixture: ComponentFixture<RestoreStudentAccountDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RestoreStudentAccountDialogComponent]
    });
    fixture = TestBed.createComponent(RestoreStudentAccountDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
