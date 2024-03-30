import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RestoreTeacherAccountDialogComponent } from './restore-teacher-account-dialog.component';

describe('RestoreTeacherAccountDialogComponent', () => {
  let component: RestoreTeacherAccountDialogComponent;
  let fixture: ComponentFixture<RestoreTeacherAccountDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RestoreTeacherAccountDialogComponent]
    });
    fixture = TestBed.createComponent(RestoreTeacherAccountDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
