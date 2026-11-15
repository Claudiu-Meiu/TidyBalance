import { Component, inject } from '@angular/core';
import { finalize } from 'rxjs';
import { Router } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import {
  RequestErrorCode,
  RequestErrorFormInputIdentifier,
  nicknamePatternValidator,
  usernamePatternValidator,
  passwordPatternValidator,
} from '../../../app.validation-form-errors';

import { TuiButton, TuiInput, TuiError, TuiTitle, TuiIcon } from '@taiga-ui/core';
import { TuiPassword, TuiButtonLoading } from '@taiga-ui/kit';
import { TuiCardLarge, TuiForm, TuiHeader } from '@taiga-ui/layout';
import { HttpErrorResponse, HttpStatusCode } from '@angular/common/http';
import { AppRoutesPagesEnum } from '../../../app.routes-pages.enum';
import { AccountService } from '../account.service';

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
  templateUrl: './register.html',
})
export class Register {
  private accountService = inject(AccountService);
  private router = inject(Router);

  protected readonly requestErrorCode = RequestErrorCode;
  protected readonly requestErrorFormInputIdentifier = RequestErrorFormInputIdentifier;

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

  isRegisterFormSubmitting = false;

  register() {
    if (this.registerForm.invalid || this.isRegisterFormSubmitting) {
      this.registerForm.markAllAsTouched();
      return;
    }

    this.isRegisterFormSubmitting = true;

    this.accountService
      .register({
        nickname: this.registerForm.controls.nickname.value,
        username: this.registerForm.controls.username.value,
        password: this.registerForm.controls.password.value,
      })
      .pipe(finalize(() => (this.isRegisterFormSubmitting = false)))
      .subscribe({
        next: () => {
          this.router.navigate([AppRoutesPagesEnum.PAGE_LOGIN]);
        },

        error: (error: HttpErrorResponse) => {
          if (error.status === HttpStatusCode.Conflict) {
            switch (error.error.code) {
              case this.requestErrorCode.NICKNAME_TAKEN:
                this.registerForm.controls.nickname.setErrors({
                  [this.requestErrorFormInputIdentifier.nicknameTaken]: true,
                });
                break;

              case this.requestErrorCode.USERNAME_TAKEN:
                this.registerForm.controls.username.setErrors({
                  [this.requestErrorFormInputIdentifier.usernameTaken]: true,
                });
                break;
            }
          } else if (error.status === 0 || error.status === HttpStatusCode.BadGateway) {
            this.registerForm.setErrors({
              [this.requestErrorFormInputIdentifier.serverUnavailable]: true,
            });
          }
        },
      });
  }

  backToLoginBtn() {
    this.router.navigate([AppRoutesPagesEnum.PAGE_LOGIN]);
  }
}
