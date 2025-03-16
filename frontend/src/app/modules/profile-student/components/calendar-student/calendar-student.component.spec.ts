import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CalendarStudentComponent } from './calendar-student.component';

describe('CalendarStudentComponent', () => {
  let component: CalendarStudentComponent;
  let fixture: ComponentFixture<CalendarStudentComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CalendarStudentComponent]
    });
    fixture = TestBed.createComponent(CalendarStudentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
