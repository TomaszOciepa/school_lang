import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfileStudentComponent } from './profile-student.component';

describe('ProfileStudentComponent', () => {
  let component: ProfileStudentComponent;
  let fixture: ComponentFixture<ProfileStudentComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProfileStudentComponent]
    });
    fixture = TestBed.createComponent(ProfileStudentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
