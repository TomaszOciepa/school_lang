import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/modules/core/models/user.model';
import { LoadUserProfileService } from 'src/app/modules/core/services/load-user-profile.service';
import { StudentAccountService } from 'src/app/modules/core/services/student-account.service';
import { StudentService } from 'src/app/modules/core/services/student.service';
import { MatDialog } from '@angular/material/dialog';
import { EditStudentDialogComponent } from '../../../students/components/student/edit-student-dialog/edit-student-dialog.component';

@Component({
  selector: 'app-my-account',
  templateUrl: './my-account.component.html',
  styleUrls: ['./my-account.component.css'],
})
export class MyAccountComponent implements OnInit {
  student!: User;

  constructor(
    private studentAccount: StudentAccountService,
    private studentService: StudentService,
    private dialog: MatDialog
  ) {}

  async ngOnInit(): Promise<void> {
    await this.loadUserProfile();
    this.getStudentById(this.studentAccount.userId);
  }

  async loadUserProfile(): Promise<void> {
    await this.studentAccount.loadUserProfile();
  }

  private getStudentById(id: number) {
    console.log('id-=: ' + id);
    this.studentService.getStudentById(id).subscribe({
      next: (response) => {
        this.student = response;
      },
      error: (err: Error) => {
        console.log(err);
      },
      complete: () => {},
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

  getStudentStatus(status: any): string {
    switch (status) {
      case 'ACTIVE':
        return 'Aktywne';
      case 'INACTIVE':
        return 'Niekatywne';
      case 'REMOVED':
        return 'Usuniete';
      default:
        return '...';
    }
  }
}
