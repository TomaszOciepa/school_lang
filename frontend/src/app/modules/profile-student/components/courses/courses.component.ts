import { Component } from '@angular/core';
import { StudentAccountService } from 'src/app/modules/core/services/student-account.service';

@Component({
  selector: 'app-courses',
  templateUrl: './courses.component.html',
  styleUrls: ['./courses.component.css'],
})
export class CoursesComponent {
  userId!: number;

  constructor(private studentAccount: StudentAccountService) {}

  ngOnInit(): void {
    this.loadUserProfile();
  }

  async loadUserProfile(): Promise<void> {
    await this.studentAccount.loadUserProfile();
    this.userId = this.studentAccount.userId;
  }
}
