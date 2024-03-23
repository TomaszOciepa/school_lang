import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CoursesTableComponent } from './courses-table.component';

describe('CoursesTableComponent', () => {
  let component: CoursesTableComponent;
  let fixture: ComponentFixture<CoursesTableComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CoursesTableComponent]
    });
    fixture = TestBed.createComponent(CoursesTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
