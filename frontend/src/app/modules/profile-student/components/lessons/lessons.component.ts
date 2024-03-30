import { Component } from '@angular/core';
import { StudentAccountService } from 'src/app/modules/core/services/student-account.service';

@Component({
  selector: 'app-lessons',
  templateUrl: './lessons.component.html',
  styleUrls: ['./lessons.component.css'],
})
export class LessonsComponent {
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
