import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PaymentsRoutingModule } from './payments-routing.module';
import { PaymentsComponent } from './payments.component';
import { SharedModule } from '../shared/shared.module';
import { PaymentsTableComponent } from './components/payments-table/payments-table.component';
import { PaymentsDeleteDialogComponent } from './components/payments-delete-dialog/payments-delete-dialog.component';

@NgModule({
  declarations: [PaymentsComponent, PaymentsTableComponent, PaymentsDeleteDialogComponent],
  imports: [SharedModule, CommonModule, PaymentsRoutingModule],
})
export class PaymentsModule {}
