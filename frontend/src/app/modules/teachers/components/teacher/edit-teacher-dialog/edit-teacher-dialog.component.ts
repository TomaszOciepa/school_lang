import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { User } from 'src/app/modules/core/models/user.model';

@Component({
  selector: 'app-edit-teacher-dialog',
  templateUrl: './edit-teacher-dialog.component.html',
  styleUrls: ['./edit-teacher-dialog.component.css'],
})
export class EditTeacherDialogComponent {
  constructor(
    private dialogRef: MatDialogRef<EditTeacherDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { teacher: User }
  ) {}

  closeDialog() {
    this.dialogRef.close();
  }
}
