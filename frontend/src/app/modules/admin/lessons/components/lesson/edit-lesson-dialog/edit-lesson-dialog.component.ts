import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Lesson } from 'src/app/modules/core/models/lesson.model';

@Component({
  selector: 'app-edit-lesson-dialog',
  templateUrl: './edit-lesson-dialog.component.html',
  styleUrls: ['./edit-lesson-dialog.component.css'],
})
export class EditLessonDialogComponent {
  constructor(
    private dialogRef: MatDialogRef<EditLessonDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { lesson: Lesson }
  ) {}

  closeDialog() {
    this.dialogRef.close();
  }
}
