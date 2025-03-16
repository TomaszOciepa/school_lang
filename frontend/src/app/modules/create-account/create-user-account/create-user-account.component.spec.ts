import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateStudentAccountComponent } from './create-user-account.component';

describe('CreateStudentAccountComponent', () => {
  let component: CreateStudentAccountComponent;
  let fixture: ComponentFixture<CreateStudentAccountComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CreateStudentAccountComponent],
    });
    fixture = TestBed.createComponent(CreateStudentAccountComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
