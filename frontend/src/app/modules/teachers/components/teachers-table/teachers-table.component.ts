import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TeacherResponse } from 'src/app/modules/core/models/teacher.model';
import { TeacherService } from 'src/app/modules/core/services/teacher.service';

@Component({
  selector: 'app-teachers-table',
  templateUrl: './teachers-table.component.html',
  styleUrls: ['./teachers-table.component.css'],
})
export class TeachersTableComponent implements OnInit {
  constructor(private teacherService: TeacherService, private router: Router) {}

  ngOnInit(): void {
    this.getTeachers();
  }

  teachersList: TeacherResponse[] = [];
  id!: number;

  getTeachers() {
    this.teacherService.getTeacher().subscribe((response) => {
      this.teachersList = response;
    });
  }

  details(id: number) {
    this.router.navigate(['/teachers', id]);
  }
}
