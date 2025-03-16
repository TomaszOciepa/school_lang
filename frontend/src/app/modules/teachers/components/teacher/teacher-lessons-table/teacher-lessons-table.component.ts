import {
  Component,
  ErrorHandler,
  Input,
  OnChanges,
  SimpleChanges,
  ViewChild,
} from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { Lesson } from 'src/app/modules/core/models/lesson.model';
import { LessonsService } from 'src/app/modules/core/services/lessons.service';
import { LoadUserProfileService } from 'src/app/modules/core/services/load-user-profile.service';

@Component({
  selector: 'app-teacher-lessons-table',
  templateUrl: './teacher-lessons-table.component.html',
  styleUrls: ['./teacher-lessons-table.component.css'],
})
export class TeacherLessonsTableComponent implements OnChanges {
  @Input('select-course-id') selectCourseId!: string;

  ngOnChanges(changes: SimpleChanges) {
    if (changes['selectCourseId'] && changes['selectCourseId'].currentValue) {
      this.getLessonsById(changes['selectCourseId'].currentValue);
    }
  }

  displayedColumns: string[] = [
    'lp',
    'startDate',
    'hours',
    'language',
    'status',
  ];
  dataSource!: MatTableDataSource<Lesson>;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  @Input('teacher-id') teacherId!: number;

  lessonsNumber!: number;
  groupLessonsMap!: Map<string, Lesson[]>;
  errMsg!: string;
  role!: string;

  constructor(
    private userProfileService: LoadUserProfileService,
    private lessonsService: LessonsService,
    private router: Router
  ) {}

  async ngOnInit(): Promise<void> {
    this.loadUserProfile();
    this.getLessonsByTeacherId();
  }

  async loadUserProfile(): Promise<void> {
    await this.userProfileService.loadUserProfile();
    if (this.userProfileService.isAdmin) {
      this.role = 'ADMIN';
    }
  }

  private getLessonsByTeacherId() {
    this.lessonsService.getLessonByTeacherId(this.teacherId).subscribe({
      next: (lesson) => {
        this.lessonsNumber = lesson.length;
        this.groupLessonsByCourse(lesson);
      },
      error: (err: ErrorHandler) => {
        console.log(err);
      },
    });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  addLesson() {
    this.router.navigate(['/lessons/dodaj', { teacherId: this.teacherId }]);
  }

  navigateToLesson(lessonId: string) {
    this.router.navigate(['/lessons', lessonId]);
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

  getLessonsById(courseId: string) {
    const lessons: Lesson[] = this.groupLessonsMap.get(courseId) || [];
    this.dataSource = new MatTableDataSource<Lesson>(lessons);
  }

  groupLessonsByCourse(lessons: Lesson[]) {
    const lessonsMap = new Map<string, Lesson[]>();

    lessons.forEach((lesson) => {
      if (!lessonsMap.has(lesson.courseId)) {
        lessonsMap.set(lesson.courseId, []);
      }
      lessonsMap.get(lesson.courseId)?.push(lesson);
    });

    this.groupLessonsMap = lessonsMap;
  }
}
