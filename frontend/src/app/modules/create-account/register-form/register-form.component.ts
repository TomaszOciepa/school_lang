import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { PostUserForm, User } from '../../core/models/user.model';
import { Observer } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsService } from '../../core/services/forms.service';
import { RegisterService } from '../../core/services/register.service';

@Component({
  selector: 'app-register-form',
  templateUrl: './register-form.component.html',
  styleUrls: ['./register-form.component.css'],
})
export class RegisterFormComponent implements OnInit {
  userType: string | null = null;
  userForm!: FormGroup<PostUserForm>;
  @Input() student!: User;
  @Output() closeDialog = new EventEmitter<void>();
  observer: Observer<unknown> = {
    next: () => {},
    error: (err) => {
      console.log(err);
    },
    complete: () => {
      // this.router.navigate(['/students/']);
    },
  };

  constructor(
    private formService: FormsService,
    private registerService: RegisterService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.route.paramMap.subscribe((params) => {
      this.userType = params.get('userType');
    });
  }

  get controls() {
    return this.userForm.controls;
  }

  private initForm() {
    this.userForm = new FormGroup({
      firstName: new FormControl('', {
        nonNullable: true,
        validators: [
          Validators.required,
          Validators.minLength(3),
          Validators.maxLength(20),
        ],
      }),
      lastName: new FormControl('', {
        nonNullable: true,
        validators: [
          Validators.required,
          Validators.minLength(4),
          Validators.maxLength(20),
        ],
      }),
      email: new FormControl('', {
        nonNullable: true,
        validators: [Validators.required, Validators.email],
      }),
    });
  }

  getErrorMessage(control: FormControl) {
    return this.formService.getErrorMessage(control);
  }

  createAccount() {
    if (this.userType == 'student') {
      this.registerService
        .createAccountStudent(this.userForm.getRawValue())
        .subscribe(this.observer);
      return;
    }

    if (this.userType == 'teacher') {
      this.registerService
        .createAccountTeacher(this.userForm.getRawValue())
        .subscribe(this.observer);
      return;
    }
  }
}
