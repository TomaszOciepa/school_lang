import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PaymentsDeleteDialogComponent } from './payments-delete-dialog.component';

describe('PaymentsDeleteDialogComponent', () => {
  let component: PaymentsDeleteDialogComponent;
  let fixture: ComponentFixture<PaymentsDeleteDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PaymentsDeleteDialogComponent]
    });
    fixture = TestBed.createComponent(PaymentsDeleteDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
