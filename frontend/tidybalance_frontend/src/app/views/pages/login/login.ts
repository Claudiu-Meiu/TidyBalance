import { Component, inject } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
  FormsModule,
} from '@angular/forms';
import { Router } from '@angular/router';
import {
  TuiButton,
  TuiInput,
  TuiError,
  TuiTitle,
  TuiIcon,
  TUI_VALIDATION_ERRORS,
} from '@taiga-ui/core';
import { TuiPassword } from '@taiga-ui/kit';
import { TuiCardLarge, TuiForm, TuiHeader } from '@taiga-ui/layout';
import { Auth } from '../../../auth/auth';
import { HttpErrorResponse, HttpStatusCode } from '@angular/common/http';

enum LoginFormSubmitErrors {
  invalidCredentials = 'invalidCredentials',
  serverUnavailable = 'serverUnavailable',
}

@Component({
  imports: [
    ReactiveFormsModule,
    TuiButton,
    TuiInput,
    TuiError,
    TuiForm,
    TuiCardLarge,
    TuiHeader,
    TuiTitle,
    TuiPassword,
    TuiIcon,
    FormsModule,
  ],
  selector: 'app-login',
  styleUrl: './login.css',
  templateUrl: './login.html',
  providers: [
    {
      provide: TUI_VALIDATION_ERRORS,
      useValue: {
        required: 'This field is required.',
      },
    },
  ],
})
export class Login {
  private authService = inject(Auth);
  private router = inject(Router);

  readonly loginFormSubmitErrors = LoginFormSubmitErrors

  protected readonly loginForm = new FormGroup({
    username: new FormControl('', {
      validators: [Validators.required],
      nonNullable: true,
    }),

    password: new FormControl('', {
      validators: [Validators.required],
      nonNullable: true,
    }),
  });

  login() {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    this.loginForm.setErrors(null);

    this.authService
      .login({
        username: this.loginForm.controls.username.value,
        password: this.loginForm.controls.password.value,
      })
      .subscribe({
        next: () => {
          this.router.navigate(['/home']);
        },
        error: (error: HttpErrorResponse) => {
          if (error.status === HttpStatusCode.Unauthorized) {
            this.loginForm.setErrors({
              [this.loginFormSubmitErrors.invalidCredentials]: true,
            });
          } else if (error.status === 0) {
            this.loginForm.setErrors({
              [this.loginFormSubmitErrors.serverUnavailable]: true,
            });
          }
        },
      });
  }

  createNewAccountBtn() {
    this.router.navigate(['/register']);
  }
}
