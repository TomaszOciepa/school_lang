import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LesonsTableComponent } from './lesons-table.component';

describe('LesonsTableComponent', () => {
  let component: LesonsTableComponent;
  let fixture: ComponentFixture<LesonsTableComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LesonsTableComponent]
    });
    fixture = TestBed.createComponent(LesonsTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
