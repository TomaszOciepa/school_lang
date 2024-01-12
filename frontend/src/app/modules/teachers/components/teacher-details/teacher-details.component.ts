import { Component, OnInit } from '@angular/core';
import { TeacherService } from 'src/app/modules/core/services/teacher.service';
import { TeacherResponse } from 'src/app/modules/core/models/teacher.model';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-teacher-details',
  templateUrl: './teacher-details.component.html',
  styleUrls: ['./teacher-details.component.css'],
})
export class TeacherDetailsComponent implements OnInit {
  id!: number;

  constructor(
    private teacherService: TeacherService,
    private router: Router,
    private route: ActivatedRoute
  ) {}
  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.id = params['id'];
    });

    this.getTeacher(this.id);
  }

  teacher!: TeacherResponse;

  getTeacher(id: number) {
    this.teacherService.getTeacherById(id).subscribe((response) => {
      this.teacher = response;
    });
  }
}
