import { Component, ErrorHandler, HostListener } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { User } from 'src/app/modules/core/models/user.model';
import { StudentService } from 'src/app/modules/core/services/student.service';
import { EditStudentDialogComponent } from './edit-student-dialog/edit-student-dialog.component';
import { DeleteStudentDialogComponent } from './delete-student-dialog/delete-student-dialog.component';
import { DropStudentDialogComponent } from './drop-student-dialog/drop-student-dialog.component';
import { RestoreStudentAccountDialogComponent } from './restore-student-account-dialog/restore-student-account-dialog.component';
import { Lesson } from 'src/app/modules/core/models/lesson.model';
import { LessonsService } from 'src/app/modules/core/services/lessons.service';

@Component({
  selector: 'app-student',
  templateUrl: './student.component.html',
  styleUrls: ['./student.component.css'],
})
export class StudentComponent {
  selectCourseId!: string;

  id!: number;
  student!: User;
  studentLessons!: Lesson[];

  constructor(
    private studentService: StudentService,
    private route: ActivatedRoute,
    private dialog: MatDialog,
    private lessonsService: LessonsService
  ) {}

  ngOnInit(): void {
    this.route.params;
    this.route.params.subscribe((params) => {
      this.id = params['id'];
    });

    this.getStudent(this.id);
    this.getLessonsByStudentId(this.id);
  }

  selectedCourse(courseId: string) {
    this.selectCourseId = courseId;
  }

  getStudent(id: number) {
    this.studentService.getStudentById(id).subscribe((response) => {
      this.student = response;
    });
  }

  private getLessonsByStudentId(id: number) {
    this.lessonsService.getLessonsByStudentId(id).subscribe({
      next: (lesson) => {
        this.studentLessons = lesson;
      },
      error: (err: ErrorHandler) => {
        console.log(err);
      },
    });
  }

  openDialog() {
    const dialogRef = this.dialog.open(DeleteStudentDialogComponent, {
      data: {
        student: this.student,
      },
    });
  }

  openRestoreDialog() {
    const dialogRef = this.dialog.open(RestoreStudentAccountDialogComponent, {
      data: {
        student: this.student,
      },
    });
  }

  openDropDialog(deleteSound: HTMLAudioElement) {
    deleteSound.play();
    const dialogRef = this.dialog.open(DropStudentDialogComponent, {
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

  activeSection: string = 'schedule';

  scrollToSection(sectionId: string): void {
    const element = document.getElementById(sectionId);
    if (element) {
      const menuHeight = 0;

      window.scrollTo({
        top: element.offsetTop - menuHeight,
        behavior: 'smooth',
      });
    }
  }

  @HostListener('window:scroll', ['$event'])
  onWindowScroll(): void {
    const sections = ['schedule', 'courses', 'salary'];
    const offset = window.scrollY;
    let active = '';

    sections.forEach((section) => {
      const element = document.getElementById(section);
      if (element) {
        const rect = element.getBoundingClientRect();
        if (
          rect.top <= window.innerHeight / 2 &&
          rect.bottom >= window.innerHeight / 2
        ) {
          active = section;
        }
      }
    });

    this.activeSection = active;
  }
}
