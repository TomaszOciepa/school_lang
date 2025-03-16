import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TeacherSalaryDetailsComponent } from './teacher-salary-details.component';

describe('TeacherSalaryDetailsComponent', () => {
  let component: TeacherSalaryDetailsComponent;
  let fixture: ComponentFixture<TeacherSalaryDetailsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TeacherSalaryDetailsComponent]
    });
    fixture = TestBed.createComponent(TeacherSalaryDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
