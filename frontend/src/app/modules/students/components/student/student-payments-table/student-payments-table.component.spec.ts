import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StudentPaymentsTableComponent } from './student-payments-table.component';

describe('StudentPaymentsTableComponent', () => {
  let component: StudentPaymentsTableComponent;
  let fixture: ComponentFixture<StudentPaymentsTableComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [StudentPaymentsTableComponent]
    });
    fixture = TestBed.createComponent(StudentPaymentsTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
