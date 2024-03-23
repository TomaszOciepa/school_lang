import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageTeachersComponent } from './manage-teachers.component';

describe('ManageTeachersComponent', () => {
  let component: ManageTeachersComponent;
  let fixture: ComponentFixture<ManageTeachersComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ManageTeachersComponent]
    });
    fixture = TestBed.createComponent(ManageTeachersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
