import { Component, ErrorHandler, OnInit } from '@angular/core';
import { TeacherService } from 'src/app/modules/core/services/teacher.service';
import { User } from 'src/app/modules/core/models/user.model';
import { ActivatedRoute } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { DeleteTeacherDialogComponent } from './delete-teacher-dialog/delete-teacher-dialog.component';
import { EditTeacherDialogComponent } from './edit-teacher-dialog/edit-teacher-dialog.component';
import { LoadUserProfileService } from 'src/app/modules/core/services/load-user-profile.service';
import { DropTeacherDialogComponent } from './drop-teacher-dialog/drop-teacher-dialog.component';
import { RestoreTeacherAccountDialogComponent } from './restore-teacher-account-dialog/restore-teacher-account-dialog.component';
import { LessonsService } from 'src/app/modules/core/services/lessons.service';
import { Lesson } from 'src/app/modules/core/models/lesson.model';
import { SalaryService } from 'src/app/modules/core/services/salary.service';

@Component({
  selector: 'app-teacher-details',
  templateUrl: './teacher.component.html',
  styleUrls: ['./teacher.component.css'],
})
export class TeacherDetailsComponent implements OnInit {
  selectCourseId!: string;
  id!: number;
  teacher!: User;
  role!: string;
  teacherLessons!: Lesson[];

  constructor(
    private userProfileService: LoadUserProfileService,
    private teacherService: TeacherService,
    private lessonsService: LessonsService,
    private salaryService: SalaryService,
    private route: ActivatedRoute,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.loadUserProfile();
    this.route.params;
    this.route.params.subscribe((params) => {
      this.id = params['id'];
    });

    this.getTeacher(this.id);
    this.getLessonsByTeacherId(this.id);
  }

  async loadUserProfile(): Promise<void> {
    await this.userProfileService.loadUserProfile();
    if (this.userProfileService.isAdmin) {
      this.role = 'ADMIN';
    }
  }

  selectedCourse(courseId: string) {
    this.selectCourseId = courseId;
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

  openDropDialog(deleteSound: HTMLAudioElement) {
    deleteSound.play();
    const dialogRef = this.dialog.open(DropTeacherDialogComponent, {
      data: {
        teacher: this.teacher,
      },
    });
  }

  openRestoreDialog() {
    const dialogRef = this.dialog.open(RestoreTeacherAccountDialogComponent, {
      data: {
        teacher: this.teacher,
      },
    });
  }

  openEditDialog() {
    console.log('techaer id: ' + this.teacher.id);
    const dialogRef = this.dialog.open(EditTeacherDialogComponent, {
      data: {
        teacher: this.teacher,
      },
      width: '600px',
      maxWidth: '600px',
    });
  }

  private getLessonsByTeacherId(id: number) {
    this.lessonsService.getLessonByTeacherId(id).subscribe({
      next: (lesson) => {
        this.teacherLessons = lesson;
      },
      error: (err: ErrorHandler) => {
        console.log(err);
      },
    });
  }

  getTeacherStatus(status: any): string {
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

  createSalary(id: number) {
    console.log('create salary id: ' + id);
    this.salaryService.createSalary(id).subscribe({
      next: () => console.log('poszło'),
      error: (error) => console.error('Error creating salary', error),
    });
  }
}
