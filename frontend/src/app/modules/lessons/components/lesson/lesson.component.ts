import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { Course } from 'src/app/modules/core/models/course.model';
import { Lesson } from 'src/app/modules/core/models/lesson.model';
import { User } from 'src/app/modules/core/models/user.model';
import { CourseService } from 'src/app/modules/core/services/course.service';
import { LessonsService } from 'src/app/modules/core/services/lessons.service';
import { TeacherService } from 'src/app/modules/core/services/teacher.service';
import { EditLessonDialogComponent } from './edit-lesson-dialog/edit-lesson-dialog.component';
import { DeleteLessonDialogComponent } from './delete-lesson-dialog/delete-lesson-dialog.component';
import { EnrollLessonDialogComponent } from './enroll-lesson-dialog/enroll-lesson-dialog.component';
import { LoadUserProfileService } from 'src/app/modules/core/services/load-user-profile.service';

@Component({
  selector: 'app-lesson',
  templateUrl: './lesson.component.html',
  styleUrls: ['./lesson.component.css'],
})
export class LessonComponent implements OnInit {
  id!: string;
  lesson!: Lesson;
  teacher!: User;
  course!: Course;
  role!: string;

  constructor(
    private userProfileService: LoadUserProfileService,
    private lessonsService: LessonsService,
    private teacherService: TeacherService,
    private courseService: CourseService,
    private route: ActivatedRoute,
    private dialog: MatDialog
  ) {}

  async ngOnInit(): Promise<void> {
    this.route.params;
    this.route.params.subscribe((params) => {
      this.id = params['id'];
    });
    this.loadUserProfile();
    this.getLesson(this.id);
  }

  async loadUserProfile(): Promise<void> {
    await this.userProfileService.loadUserProfile();

    if (this.userProfileService.isAdmin) {
      this.role = 'ADMIN';
    }
  }

  getLesson(id: string) {
    this.lessonsService.getLessonById(id).subscribe({
      next: (response) => {
        this.lesson = response;
      },
      error: (err) => {
        console.log(err);
      },
      complete: () => {
        this.getTeacher(this.lesson.teacherId);
        if (this.lesson.courseId !== '' && this.lesson.courseId !== null) {
          this.getCourse(this.lesson.courseId);
        }
      },
    });
  }

  getTeacher(id: number) {
    this.teacherService.getTeacherById(id).subscribe((response) => {
      this.teacher = response;
    });
  }

  getCourse(id: string) {
    this.courseService.getCourseById(id).subscribe((response) => {
      this.course = response;
    });
  }

  openEditDialog() {
    const dialogRef = this.dialog.open(EditLessonDialogComponent, {
      data: {
        lesson: this.lesson,
      },
      width: '600px',
      maxWidth: '600px',
    });
  }

  openDialog(deleteSound: HTMLAudioElement) {
    deleteSound.play();
    const dialogRef = this.dialog.open(DeleteLessonDialogComponent, {
      data: {
        lesson: this.lesson,
      },
    });
  }

  openEnrollDialog() {
    const dialogRef = this.dialog.open(EnrollLessonDialogComponent, {
      data: {
        lessonId: this.lesson.id,
        listUserId: this.lesson.attendanceList.map(
          (student) => student.studentId
        ),
      },
      width: '600px',
      maxWidth: '600px',
    });
  }

  getLanguageName(language: string): string {
    switch (language) {
      case 'ENGLISH':
        return 'Angielski';
      case 'POLISH':
        return 'Polski';
      case 'GERMAN':
        return 'Niemiecki';
      default:
        return 'Nieznany';
    }
  }

  getStatusName(status: string): string {
    switch (status) {
      case 'ACTIVE':
        return 'Aktywna';
      case 'INACTIVE':
        return 'Oczekiwana';
      case 'FINISHED':
        return 'Zakończona';
      default:
        return 'Nieznany';
    }
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'ACTIVE':
        return 'green';
      case 'INACTIVE':
        return 'orange';
      case 'FINISHED':
        return 'gray';
      default:
        return '';
    }
  }
}
