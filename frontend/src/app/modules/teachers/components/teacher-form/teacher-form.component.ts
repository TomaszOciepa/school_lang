import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Observer } from 'rxjs';
import { PostUserForm, User } from 'src/app/modules/core/models/user.model';
import { FormsService } from 'src/app/modules/core/services/forms.service';
import { TeacherService } from 'src/app/modules/core/services/teacher.service';

@Component({
  selector: 'app-teacher-form',
  templateUrl: './teacher-form.component.html',
  styleUrls: ['./teacher-form.component.css'],
})
export class TeacherFormComponent implements OnInit {
  teacherForm!: FormGroup<PostUserForm>;
  @Input() editMode = false;
  @Input() teacher!: User;
  @Output() closeDialog = new EventEmitter<void>();
  observer: Observer<unknown> = {
    next: (teacher) => {
      if (this.editMode) {
        this.emitCLoseDialog();
      }
      console.log('Zapisano do bazy: ' + teacher);
      this.router.navigate(['/teachers']);
    },
    error: (err) => {
      console.log(err);
    },
    complete: () => {},
  };

  constructor(
    private formService: FormsService,
    private teacherService: TeacherService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.initForm();
  }

  get controls() {
    return this.teacherForm.controls;
  }

  private initForm() {
    this.teacherForm = new FormGroup({
      firstName: new FormControl(this.editMode ? this.teacher.firstName : '', {
        nonNullable: true,
        validators: [
          Validators.required,
          Validators.minLength(3),
          Validators.maxLength(20),
        ],
      }),
      lastName: new FormControl(this.editMode ? this.teacher.lastName : '', {
        nonNullable: true,
        validators: [
          Validators.required,
          Validators.minLength(4),
          Validators.maxLength(20),
        ],
      }),
      email: new FormControl(this.editMode ? this.teacher.email : '', {
        nonNullable: true,
        validators: [Validators.required, Validators.email],
      }),
    });
  }

  getErrorMessage(control: FormControl) {
    return this.formService.getErrorMessage(control);
  }

  onAddTeacher() {
    if (this.editMode) {
      this.teacherService
        .patchTeacher(this.teacher.id, this.teacherForm.getRawValue())
        .subscribe(this.observer);
      return;
    }
    this.teacherService
      .addNewTeacher(this.teacherForm.getRawValue())
      .subscribe(this.observer);
  }

  emitCLoseDialog() {
    this.closeDialog.emit();
  }
}
