import { Component, ErrorHandler } from '@angular/core';
import { Lesson } from 'src/app/modules/core/models/lesson.model';
import { LessonsService } from 'src/app/modules/core/services/lessons.service';
import { StudentAccountService } from 'src/app/modules/core/services/student-account.service';

@Component({
  selector: 'app-lessons',
  templateUrl: './lessons.component.html',
  styleUrls: ['./lessons.component.css'],
})
export class LessonsComponent {
  userId!: number;
  lessonsList!: Lesson[];

  constructor(
    private studentAccount: StudentAccountService,
    private lessonsService: LessonsService
  ) {}

  ngOnInit(): void {
    this.loadUserProfile();
  }

  async loadUserProfile(): Promise<void> {
    await this.studentAccount.loadUserProfile();
    this.userId = this.studentAccount.userId;

    this.getLessonsByStudentId(this.userId);
  }

  private getLessonsByStudentId(id: number) {
    this.lessonsService.getLessonsByStudentId(id).subscribe({
      next: (lesson) => {
        this.lessonsList = lesson;
      },
      error: (err: ErrorHandler) => {
        console.log(err);
      },
    });
  }
}
