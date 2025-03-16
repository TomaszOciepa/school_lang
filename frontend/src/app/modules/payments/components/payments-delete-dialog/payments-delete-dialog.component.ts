import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { Order } from 'src/app/modules/core/models/order.model';
import { OrderService } from 'src/app/modules/core/services/order.service';

@Component({
  selector: 'app-payments-delete-dialog',
  templateUrl: './payments-delete-dialog.component.html',
  styleUrls: ['./payments-delete-dialog.component.css'],
})
export class PaymentsDeleteDialogComponent {
  id!: string;
  errorMessage = '';

  constructor(
    private dialogRef: MatDialogRef<PaymentsDeleteDialogComponent>,
    @Inject(MAT_DIALOG_DATA) private data: { id: string },
    private orderService: OrderService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.id = this.data.id;
  }

  onDelete() {
    console.log('siema: id: ' + this.id);
    this.orderService.deleteOrderById(this.id).subscribe({
      next: () => {
        this.dialogRef.close();
      },
      error: (err) => {
        this.errorMessage = err;
      },
      complete: () => {
        window.location.reload();
      },
    });
  }
}
