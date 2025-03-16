import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { User } from 'src/app/modules/core/models/user.model';
import { TeacherService } from 'src/app/modules/core/services/teacher.service';

@Component({
  selector: 'app-drop-teacher-dialog',
  templateUrl: './drop-teacher-dialog.component.html',
  styleUrls: ['./drop-teacher-dialog.component.css'],
})
export class DropTeacherDialogComponent implements OnInit {
  teacher!: User;
  errorMessage = '';

  constructor(
    private dialogRef: MatDialogRef<DropTeacherDialogComponent>,
    @Inject(MAT_DIALOG_DATA) private data: { teacher: User },
    private teacherService: TeacherService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.teacher = this.data.teacher;
  }

  onDrop() {
    this.teacherService.deleteTeacherById(this.teacher.id).subscribe({
      next: () => {
        this.dialogRef.close();
        this.router.navigate(['/teachers']);
      },
      error: (err) => {
        this.errorMessage = err;
      },
    });
  }
}
