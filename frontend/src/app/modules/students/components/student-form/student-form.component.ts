import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { Observer } from 'rxjs';
import { PostUserForm, User } from 'src/app/modules/core/models/user.model';
import { FormsService } from 'src/app/modules/core/services/forms.service';
import { StudentService } from 'src/app/modules/core/services/student.service';

@Component({
  selector: 'app-student-form',
  templateUrl: './student-form.component.html',
  styleUrls: ['./student-form.component.css'],
})
export class StudentFormComponent {
  studentForm!: FormGroup<PostUserForm>;
  @Input() editMode = false;
  @Input() student!: User;
  @Output() closeDialog = new EventEmitter<void>();
  observer: Observer<unknown> = {
    next: (student) => {
      if (this.editMode) {
        this.emitCLoseDialog();
      }
      console.log('Zapisano do bazy: ' + student);
      this.router.navigate(['/students']);
    },
    error: (err) => {
      console.log(err);
    },
    complete: () => {},
  };

  constructor(
    private formService: FormsService,
    private studentService: StudentService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.initForm();
  }

  get controls() {
    return this.studentForm.controls;
  }

  private initForm() {
    this.studentForm = new FormGroup({
      firstName: new FormControl(this.editMode ? this.student.firstName : '', {
        nonNullable: true,
        validators: [
          Validators.required,
          Validators.minLength(3),
          Validators.maxLength(20),
        ],
      }),
      lastName: new FormControl(this.editMode ? this.student.lastName : '', {
        nonNullable: true,
        validators: [
          Validators.required,
          Validators.minLength(4),
          Validators.maxLength(20),
        ],
      }),
      email: new FormControl(this.editMode ? this.student.email : '', {
        nonNullable: true,
        validators: [Validators.required, Validators.email],
      }),
    });
  }

  getErrorMessage(control: FormControl) {
    return this.formService.getErrorMessage(control);
  }

  onAddStudent() {
    if (this.editMode) {
      this.studentService
        .patchStudent(this.student.id, this.studentForm.getRawValue())
        .subscribe(this.observer);
      return;
    }
    this.studentService
      .addNewStudent(this.studentForm.getRawValue())
      .subscribe(this.observer);
  }

  emitCLoseDialog() {
    this.closeDialog.emit();
  }
}
