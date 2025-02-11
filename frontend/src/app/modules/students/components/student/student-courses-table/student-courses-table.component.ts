import { SelectionModel } from '@angular/cdk/collections';
import {
  Component,
  ErrorHandler,
  EventEmitter,
  Input,
  Output,
  ViewChild,
} from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { Course } from 'src/app/modules/core/models/course.model';
import { CourseService } from 'src/app/modules/core/services/course.service';

@Component({
  selector: 'app-student-courses-table',
  templateUrl: './student-courses-table.component.html',
  styleUrls: ['./student-courses-table.component.css'],
})
export class StudentCoursesTableComponent {
  @Output() selectedCourseId = new EventEmitter<string>();

  getSelectedCourseId(id: string): void {
    this.selectedCourseId.emit(id);
  }

  displayedColumns: string[] = [
    'lp',
    'startDate',
    'language',
    'status',
    'name',
    'select',
  ];
  dataSource!: MatTableDataSource<Course>;

  selection = new SelectionModel<any>(false, []);

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  @Input('studentId') studentId!: number;

  constructor(private courseService: CourseService, private router: Router) {}

  async ngOnInit(): Promise<void> {
    console.log();
    this.courseService.getCourseByStudentId(this.studentId).subscribe({
      next: (course) => {
        const sortedCourses = course.sort(
          (a, b) =>
            new Date(b.startDate).getTime() - new Date(a.startDate).getTime()
        );

        this.dataSource = new MatTableDataSource<Course>(sortedCourses);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
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

  navigateToCourse(courseId: string) {
    this.router.navigate(['/courses', courseId]);
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

  showLesson(id: string) {
    if (!this.selection.isSelected(id)) {
      this.selection.clear();
      this.selection.select(id);
      this.selectedCourseId.emit(id);
    } else {
      this.selection.deselect(id);
      this.selectedCourseId.emit('close');
    }
  }
}
