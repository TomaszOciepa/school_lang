import { Injectable } from '@angular/core';
import { PostUser } from '../models/user.model';
import { LoadUserProfileService } from './load-user-profile.service';
import { StudentService } from './student.service';
import { EMPTY, Observable, catchError, map } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class FirstLoginStudentAccountService {
  constructor(
    private userProfileService: LoadUserProfileService,
    private studentService: StudentService
  ) {}

  public createNewStudent(): Observable<number> {
    const newStudent: PostUser = {
      email: this.userProfileService.userProfile?.email,
      firstName: this.userProfileService.userProfile?.firstName,
      lastName: this.userProfileService.userProfile?.lastName,
    };

    return this.studentService.addNewStudent(newStudent).pipe(
      map((response) => response.id),
      catchError((error) => {
        console.log(error);
        return EMPTY;
      })
    );
  }
}
