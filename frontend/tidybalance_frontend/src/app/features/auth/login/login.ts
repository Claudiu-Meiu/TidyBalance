import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { HttpErrorResponse, HttpStatusCode } from '@angular/common/http';
import { finalize } from 'rxjs';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

import { AuthService } from '../auth.service';

import { AppRoutesPagesEnum } from '../../../app.routes-pages.enum';
import { RequestErrorFormInputIdentifier } from '../../../app.validation-form-errors';

import { TuiButton, TuiInput, TuiCheckbox, TuiError, TuiTitle, TuiIcon } from '@taiga-ui/core';
import { TuiAutoFocus } from '@taiga-ui/cdk';
import { TuiPassword, TuiButtonLoading } from '@taiga-ui/kit';
import { TuiCardLarge, TuiForm, TuiHeader } from '@taiga-ui/layout';

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
    TuiAutoFocus,
  ],
  selector: 'app-login',
  styleUrl: './login.css',
  templateUrl: './login.html',
})
export class Login {
  private authService = inject(AuthService);
  private router = inject(Router);

  protected readonly requestErrorFormInputIdentifier = RequestErrorFormInputIdentifier;

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
              [this.requestErrorFormInputIdentifier.invalidLogin]: true,
            });
          } else if (error.status === 0 || error.status === HttpStatusCode.BadGateway) {
            this.loginForm.setErrors({
              [this.requestErrorFormInputIdentifier.serverUnavailable]: true,
            });
          }
        },
      });
  }

  createNewAccountBtn() {
    this.router.navigate([AppRoutesPagesEnum.PAGE_REGISTER]);
  }
}
