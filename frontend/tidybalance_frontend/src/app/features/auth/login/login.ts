import { Component, inject } from '@angular/core';
import { finalize } from 'rxjs';
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
  TuiCheckbox,
  TuiError,
  TuiTitle,
  TuiIcon,
  TUI_VALIDATION_ERRORS,
} from '@taiga-ui/core';
import { TuiPassword, TuiButtonLoading } from '@taiga-ui/kit';
import { TuiCardLarge, TuiForm, TuiHeader } from '@taiga-ui/layout';
import { AuthService } from '../auth.service';
import { HttpErrorResponse, HttpStatusCode } from '@angular/common/http';
import { AppRoutesPagesEnum } from '../../../app.routes-pages.enum';

enum LoginFormSubmitErrors {
  invalidCredentials = 'invalidCredentials',
  serverUnavailable = 'serverUnavailable',
}

@Component({
  imports: [
    ReactiveFormsModule,
    TuiButton,
    TuiInput,
    TuiCheckbox,
    TuiError,
    TuiForm,
    TuiCardLarge,
    TuiHeader,
    TuiTitle,
    TuiPassword,
    TuiButtonLoading,
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
  private authService = inject(AuthService);
  private router = inject(Router);

  readonly loginFormSubmitErrors = LoginFormSubmitErrors;

  protected readonly loginForm = new FormGroup({
    username: new FormControl('', {
      validators: [Validators.required],
      nonNullable: true,
    }),

    password: new FormControl('', {
      validators: [Validators.required],
      nonNullable: true,
    }),

    rememberMe: new FormControl<boolean>(true, { nonNullable: true }),
  });

  isSubmitting = false;

  login() {
    if (this.loginForm.invalid || this.isSubmitting) {
      this.loginForm.markAllAsTouched();
      return;
    }

    this.isSubmitting = true;

    this.authService
      .login({
        username: this.loginForm.controls.username.value,
        password: this.loginForm.controls.password.value,
        rememberMe: this.loginForm.controls.rememberMe.value,
      })
      .pipe(finalize(() => (this.isSubmitting = false)))
      .subscribe({
        next: () => {
          this.router.navigate([AppRoutesPagesEnum.PAGE_HOME]);
        },
        error: (error: HttpErrorResponse) => {
          if (error.status === HttpStatusCode.Unauthorized) {
            this.loginForm.setErrors({
              [this.loginFormSubmitErrors.invalidCredentials]: true,
            });
          } else if (error.status === 0 || error.status === HttpStatusCode.BadGateway) {
            this.loginForm.setErrors({
              [this.loginFormSubmitErrors.serverUnavailable]: true,
            });
          }
        },
      });
  }

  createNewAccountBtn() {
    this.router.navigate([AppRoutesPagesEnum.PAGE_REGISTER]);
  }
}
