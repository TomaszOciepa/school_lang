import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LessonFormComponent } from './lesson-form.component';

describe('LessonFormComponent', () => {
  let component: LessonFormComponent;
  let fixture: ComponentFixture<LessonFormComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LessonFormComponent]
    });
    fixture = TestBed.createComponent(LessonFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
