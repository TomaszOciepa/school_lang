import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EnrollToCourseComponent } from './enroll-to-course.component';

describe('EnrollToCourseComponent', () => {
  let component: EnrollToCourseComponent;
  let fixture: ComponentFixture<EnrollToCourseComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EnrollToCourseComponent]
    });
    fixture = TestBed.createComponent(EnrollToCourseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
