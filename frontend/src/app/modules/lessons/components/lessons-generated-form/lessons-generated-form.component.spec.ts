import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LessonsGeneratedFormComponent } from './lessons-generated-form.component';

describe('LessonsGeneratedFormComponent', () => {
  let component: LessonsGeneratedFormComponent;
  let fixture: ComponentFixture<LessonsGeneratedFormComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LessonsGeneratedFormComponent]
    });
    fixture = TestBed.createComponent(LessonsGeneratedFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
