import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DropStudentDialogComponent } from './drop-student-dialog.component';

describe('DropStudentDialogComponent', () => {
  let component: DropStudentDialogComponent;
  let fixture: ComponentFixture<DropStudentDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DropStudentDialogComponent]
    });
    fixture = TestBed.createComponent(DropStudentDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
