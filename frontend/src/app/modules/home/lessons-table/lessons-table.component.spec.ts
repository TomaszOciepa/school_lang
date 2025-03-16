import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LessonsTableComponent } from './lessons-table.component';

describe('LessonsTableComponent', () => {
  let component: LessonsTableComponent;
  let fixture: ComponentFixture<LessonsTableComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LessonsTableComponent]
    });
    fixture = TestBed.createComponent(LessonsTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
