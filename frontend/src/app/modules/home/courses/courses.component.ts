import { Component, OnInit, ViewChild } from '@angular/core';
import { CourseService } from '../../core/services/course.service';
import { ActivatedRoute, Router } from '@angular/router';
import { MatTableDataSource } from '@angular/material/table';
import { Course } from '../../core/models/course.model';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { LoadUserProfileService } from '../../core/services/load-user-profile.service';

@Component({
  selector: 'app-courses',
  templateUrl: './courses.component.html',
  styleUrls: ['./courses.component.css'],
})
export class CoursesComponent implements OnInit {
  displayedColumns: string[] = ['lp', 'name', 'language', 'date', 'price'];

  dataSource!: MatTableDataSource<Course>;
  @ViewChild(MatSort) sort!: MatSort;

  language!: string;
  title!: string;

  isLoggedIn = false;
  isAdmin: boolean = false;
  isTeacher: boolean = false;
  isStudent: boolean = false;

  constructor(
    private userProfileService: LoadUserProfileService,
    private courseService: CourseService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  async ngOnInit(): Promise<void> {
    this.route.params.subscribe((params) => {
      this.language = params['language'];
    });

    this.setLang();
    this.getCourses();

    this.loadUserProfile();
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
        this.dataSource.sort = this.sort;
      },
      error: (err: Error) => {
        console.log(err);
      },
      complete: () => {},
    });
  }

  async loadUserProfile(): Promise<void> {
    await this.userProfileService.loadUserProfile();
    this.isLoggedIn = this.userProfileService.isLoggedIn;
    this.isAdmin = this.userProfileService.isAdmin;
    this.isTeacher = this.userProfileService.isTeacher;
    this.isStudent = this.userProfileService.isStudent;
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  navigateToCourse(courseId: string) {
    this.router.navigate(['/course/', courseId]);
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
}
