import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PaymentsTableComponent } from './payments-table.component';

describe('PaymentsTableComponent', () => {
  let component: PaymentsTableComponent;
  let fixture: ComponentFixture<PaymentsTableComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PaymentsTableComponent]
    });
    fixture = TestBed.createComponent(PaymentsTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
