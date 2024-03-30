import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { User } from 'src/app/modules/core/models/user.model';
import { TeacherService } from 'src/app/modules/core/services/teacher.service';

@Component({
  selector: 'app-delete-teacher-dialog',
  templateUrl: './delete-teacher-dialog.component.html',
  styleUrls: ['./delete-teacher-dialog.component.css'],
})
export class DeleteTeacherDialogComponent implements OnInit {
  teacher!: User;
  errorMessage = '';

  constructor(
    private dialogRef: MatDialogRef<DeleteTeacherDialogComponent>,
    @Inject(MAT_DIALOG_DATA) private data: { teacher: User },
    private teacherService: TeacherService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.teacher = this.data.teacher;
  }

  onDelete() {
    this.teacherService.deactivateTeacherById(this.teacher.id).subscribe({
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
