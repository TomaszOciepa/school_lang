import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { User } from 'src/app/modules/core/models/user.model';
import { StudentService } from 'src/app/modules/core/services/student.service';
import { EditStudentDialogComponent } from './edit-student-dialog/edit-student-dialog.component';
import { DeleteStudentDialogComponent } from './delete-student-dialog/delete-student-dialog.component';

@Component({
  selector: 'app-student',
  templateUrl: './student.component.html',
  styleUrls: ['./student.component.css'],
})
export class StudentComponent {
  id!: number;
  student!: User;

  constructor(
    private studentService: StudentService,
    private route: ActivatedRoute,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.route.params;
    this.route.params.subscribe((params) => {
      this.id = params['id'];
    });

    this.getStudent(this.id);
  }

  getStudent(id: number) {
    this.studentService.getStudentById(id).subscribe((response) => {
      this.student = response;
    });
  }

  openDialog() {
    const dialogRef = this.dialog.open(DeleteStudentDialogComponent, {
      data: {
        student: this.student,
      },
    });
  }

  openEditDialog() {
    const dialogRef = this.dialog.open(EditStudentDialogComponent, {
      data: {
        student: this.student,
      },
      width: '600px',
      maxWidth: '600px',
    });
  }
}
