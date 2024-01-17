import { Component, OnInit } from '@angular/core';
import { TeacherService } from 'src/app/modules/core/services/teacher.service';
import { User } from 'src/app/modules/core/models/teacher.model';
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { DeleteTeacherDialogComponent } from './delete-teacher-dialog/delete-teacher-dialog.component';
import { EditTeacherDialogComponent } from './edit-teacher-dialog/edit-teacher-dialog.component';

@Component({
  selector: 'app-teacher-details',
  templateUrl: './teacher.component.html',
  styleUrls: ['./teacher.component.css'],
})
export class TeacherDetailsComponent implements OnInit {
  id!: number;
  teacher!: User;

  constructor(
    private teacherService: TeacherService,
    private route: ActivatedRoute,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.route.params;
    this.route.params.subscribe((params) => {
      this.id = params['id'];
    });

    this.getTeacher(this.id);
  }

  getTeacher(id: number) {
    this.teacherService.getTeacherById(id).subscribe((response) => {
      this.teacher = response;
    });
  }

  openDialog() {
    const dialogRef = this.dialog.open(DeleteTeacherDialogComponent, {
      data: {
        teacher: this.teacher,
      },
    });
  }

  openEditDialog() {
    const dialogRef = this.dialog.open(EditTeacherDialogComponent, {
      data: {
        teacher: this.teacher,
      },
      width: '600px',
      maxWidth: '600px',
    });
  }
}
