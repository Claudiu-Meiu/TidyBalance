import { Component, inject } from '@angular/core';
import { finalize } from 'rxjs';
import { Auth } from '../../../auth/auth';
import { Router } from '@angular/router';
import {
  ReactiveFormsModule,
  FormControl,
  FormGroup,
  Validators,
  AbstractControl,
  ValidationErrors,
} from '@angular/forms';
import {
  TuiButton,
  TuiInput,
  TuiError,
  TuiTitle,
  TuiIcon,
  TUI_VALIDATION_ERRORS,
} from '@taiga-ui/core';
import { TuiPassword, TuiButtonLoading } from '@taiga-ui/kit';
import { TuiCardLarge, TuiForm, TuiHeader } from '@taiga-ui/layout';
import { HttpErrorResponse, HttpStatusCode } from '@angular/common/http';

enum RegisterErrorCode {
  NICKNAME_TAKEN = 'NICKNAME_TAKEN',
  USERNAME_TAKEN = 'USERNAME_TAKEN',
}

enum RegisterFormError {
  nicknameTaken = 'nicknameTaken',
  usernameTaken = 'usernameTaken',
  serverUnavailable = 'serverUnavailable',
}

function nicknamePatternValidator(control: AbstractControl): ValidationErrors | null {
  const username: string = control.value ?? '';
  const message = 'Nickname may contain only one space between words.';
  const pattern: RegExp = /^\S+(?: \S+)*$/;

  return pattern.test(username) ? null : { other: message };
}

function usernamePatternValidator(control: AbstractControl): ValidationErrors | null {
  const username: string = control.value ?? '';
  const message = 'Username must not contain whitespaces.';
  const pattern: RegExp = /^\S+$/;

  return pattern.test(username) ? null : { other: message };
}

function passwordPatternValidator(control: AbstractControl): ValidationErrors | null {
  const password: string = control.value ?? '';
  const message =
    'Password must contain at least one uppercase letter, one lowercase letter, one number, one special character, and no whitespaces.';
  const pattern: RegExp = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z\d\s])\S+$/;

  return pattern.test(password) ? null : { other: message };
}

@Component({
  imports: [
    ReactiveFormsModule,
    TuiButton,
    TuiInput,
    TuiError,
    TuiTitle,
    TuiIcon,
    TuiPassword,
    TuiButtonLoading,
    TuiCardLarge,
    TuiForm,
    TuiHeader,
  ],
  selector: 'app-register',
  styleUrl: './register.css',
  templateUrl: './register.html',
  providers: [
    {
      provide: TUI_VALIDATION_ERRORS,
      useFactory: () => ({
        required: 'This field is required.',
        minlength: ({ requiredLength }: { requiredLength: number }) =>
          `Must be at least ${requiredLength} characters long.`,
        maxlength: ({ requiredLength }: { requiredLength: number }) =>
          `Must not exceed ${requiredLength} characters.`,
        [RegisterFormError.nicknameTaken]: 'Nickname is already taken.',
        [RegisterFormError.usernameTaken]: 'Username is already taken.',
      }),
    },
  ],
})
export class Register {
  private authService = inject(Auth);
  private router = inject(Router);

  readonly registerApiErrorCodes = RegisterErrorCode;
  readonly registerFormSubmitErrors = RegisterFormError;

  protected readonly registerForm = new FormGroup({
    nickname: new FormControl('', {
      validators: [
        Validators.required,
        Validators.minLength(4),
        Validators.maxLength(30),
        nicknamePatternValidator,
      ],
      nonNullable: true,
    }),

    username: new FormControl('', {
      validators: [
        Validators.required,
        Validators.minLength(8),
        Validators.maxLength(30),
        usernamePatternValidator,
      ],
      nonNullable: true,
    }),

    password: new FormControl('', {
      validators: [
        Validators.required,
        Validators.minLength(8),
        Validators.maxLength(50),
        passwordPatternValidator,
      ],
      nonNullable: true,
    }),
  });

  isSubmitting = false;

  register() {
    if (this.registerForm.invalid || this.isSubmitting) {
      this.registerForm.markAllAsTouched();
      return;
    }

    this.isSubmitting = true;

    this.authService
      .register({
        nickname: this.registerForm.value.nickname!,
        username: this.registerForm.value.username!,
        password: this.registerForm.value.password!,
      })
      .pipe(finalize(() => (this.isSubmitting = false)))
      .subscribe({
        next: () => {
          this.router.navigate(['/login']);
        },

        error: (error: HttpErrorResponse) => {
          if (error.status === HttpStatusCode.Conflict) {
            switch (error.error?.code) {
              case this.registerApiErrorCodes.NICKNAME_TAKEN:
                this.registerForm.controls.nickname.setErrors({
                  [this.registerFormSubmitErrors.nicknameTaken]: true,
                });
                break;

              case this.registerApiErrorCodes.USERNAME_TAKEN:
                this.registerForm.controls.username.setErrors({
                  [this.registerFormSubmitErrors.usernameTaken]: true,
                });
                break;
            }
          } else if (error.status === 0 || error.status === HttpStatusCode.BadGateway) {
            this.registerForm.setErrors({
              [this.registerFormSubmitErrors.serverUnavailable]: true,
            });
          }
        },
      });
  }

  backToLoginBtn() {
    this.router.navigate(['/login']);
  }
}
