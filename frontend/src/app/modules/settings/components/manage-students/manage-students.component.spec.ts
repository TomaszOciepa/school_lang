import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageStudentsComponent } from './manage-students.component';

describe('ManageStudentsComponent', () => {
  let component: ManageStudentsComponent;
  let fixture: ComponentFixture<ManageStudentsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ManageStudentsComponent]
    });
    fixture = TestBed.createComponent(ManageStudentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
