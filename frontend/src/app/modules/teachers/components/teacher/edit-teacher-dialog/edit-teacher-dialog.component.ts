import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Teacher } from 'src/app/modules/core/models/teacher.model';

@Component({
  selector: 'app-edit-teacher-dialog',
  templateUrl: './edit-teacher-dialog.component.html',
  styleUrls: ['./edit-teacher-dialog.component.css'],
})
export class EditTeacherDialogComponent {
  constructor(
    private dialogRef: MatDialogRef<EditTeacherDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { teacher: Teacher }
  ) {}

  closeDialog() {
    this.dialogRef.close();
  }
}
