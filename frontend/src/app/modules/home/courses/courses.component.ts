import { Component, OnInit, ViewChild } from '@angular/core';
import { CourseService } from '../../core/services/course.service';
import { ActivatedRoute } from '@angular/router';
import { MatTableDataSource } from '@angular/material/table';
import { Course } from '../../core/models/course.model';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';

@Component({
  selector: 'app-courses',
  templateUrl: './courses.component.html',
  styleUrls: ['./courses.component.css'],
})
export class CoursesComponent implements OnInit {
  displayedColumns: string[] = ['lp', 'name', 'language', 'date', 'buttons'];

  dataSource!: MatTableDataSource<Course>;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  language!: string;
  title!: string;

  constructor(
    private courseService: CourseService,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    this.route.params.subscribe((params) => {
      this.language = params['language'];
    });

    this.setLang();
    this.getCourses();
  }

  private setLang() {
    if (this.language == 'en') {
      this.language = 'ENGLISH';
      this.title = 'Angielskiego';
    }

    if (this.language == 'pl') {
      this.language = 'POLISH';
      this.title = 'Polskiego';
    }

    if (this.language == 'de') {
      this.language = 'GERMAN';
      this.title = 'Niemieckiego';
    }
  }

  private getCourses() {
    this.courseService.getCoursesByLanguage(this.language).subscribe({
      next: (course) => {
        this.dataSource = new MatTableDataSource<Course>(course);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      },
      error: (err: Error) => {
        console.log(err);
      },
      complete: () => {},
    });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }
}
