import { Component, ErrorHandler, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Order } from 'src/app/modules/core/models/order.model';
import { OrderService } from 'src/app/modules/core/services/order.service';
import { PaymentsDeleteDialogComponent } from '../payments-delete-dialog/payments-delete-dialog.component';

@Component({
  selector: 'app-payments-table',
  templateUrl: './payments-table.component.html',
  styleUrls: ['./payments-table.component.css'],
})
export class PaymentsTableComponent {
  displayedColumns: string[] = [
    'lp',
    'createDate',
    'studentId',
    'courseId',
    'orderNumber',
    'status',
    'totalAmount',
    'paymentServiceNumber',
    'buttons',
  ];
  dataSource!: MatTableDataSource<Order>;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(private orderService: OrderService, private dialog: MatDialog) {}

  async ngOnInit(): Promise<void> {
    this.getOrders();
  }

  private getOrders() {
    this.orderService.getAllOrders().subscribe({
      next: (orders) => {
        this.dataSource = new MatTableDataSource<Order>(orders);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      },
      error: (err: ErrorHandler) => {
        console.log(err);
      },
    });
  }

  openDialog(id: string) {
    console.log('id zamówienia: ' + id);
    const dialogRef = this.dialog.open(PaymentsDeleteDialogComponent, {
      data: {
        id: id,
      },
    });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }
}
