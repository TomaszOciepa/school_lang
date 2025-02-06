import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Course } from 'src/app/modules/core/models/course.model';
import { Lesson } from 'src/app/modules/core/models/lesson.model';
import { User } from 'src/app/modules/core/models/user.model';
import { CourseService } from 'src/app/modules/core/services/course.service';
import { LessonsService } from 'src/app/modules/core/services/lessons.service';
import { TeacherService } from 'src/app/modules/core/services/teacher.service';

@Component({
  selector: 'app-lesson',
  templateUrl: './lesson.component.html',
  styleUrls: ['./lesson.component.css'],
})
export class LessonComponent {
  id!: string;
  userId!: number;
  lesson!: Lesson;
  teacher!: User;
  course!: Course;
  lessonAttendance!: boolean;

  constructor(
    private lessonsService: LessonsService,
    private teacherService: TeacherService,
    private courseService: CourseService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.params;
    this.route.params.subscribe((params) => {
      this.id = params['id'];
      this.userId = params['userId'];
    });
    this.getLesson(this.id);
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

        const attendance = this.lesson.attendanceList.find(
          (s) => s.studentId == this.userId
        );
        this.lessonAttendance = attendance?.present ?? false;
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
