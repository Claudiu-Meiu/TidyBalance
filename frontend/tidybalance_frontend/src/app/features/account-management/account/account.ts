import { Component, inject, OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
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

import { UserStateService } from '../../auth/user-state.service';
import { AccountService } from '../account.service';
import { AuthService } from '../../auth/auth.service';
import { AppRoutesPagesEnum } from '../../../app.routes-pages.enum';

import {
  TUI_DARK_MODE,
  TuiInput,
  TuiButton,
  TuiTitle,
  TuiDialog,
  TuiGroup,
  TuiRadio,
  TuiError,
  TuiIcon,
} from '@taiga-ui/core';
import { TuiCardLarge, TuiHeader, TuiForm } from '@taiga-ui/layout';
import { TuiBlock, TuiPassword, TuiButtonLoading } from '@taiga-ui/kit';
import { TuiAutoFocus } from '@taiga-ui/cdk';
import { HttpErrorResponse, HttpStatusCode } from '@angular/common/http';

@Component({
  imports: [
    DatePipe,
    ReactiveFormsModule,
    TuiCardLarge,
    TuiHeader,
    TuiButton,
    TuiTitle,
    TuiDialog,
    TuiBlock,
    TuiGroup,
    TuiRadio,
    TuiInput,
    TuiPassword,
    TuiForm,
    TuiAutoFocus,
    TuiError,
    TuiIcon,
    TuiButtonLoading,
  ],
  selector: 'app-account',
  templateUrl: './account.html',
})
export class Account implements OnInit {
  private readonly darkMode = inject(TUI_DARK_MODE);
  private readonly router = inject(Router);
  private readonly userStateService = inject(UserStateService);
  private readonly authService = inject(AuthService);
  private readonly accountService = inject(AccountService);

  protected readonly user = this.userStateService.user;

  protected readonly requestErrorCode = RequestErrorCode;
  protected readonly requestErrorFormInputIdentifier = RequestErrorFormInputIdentifier;

  protected isChangePasswordDialogOpen = false;
  protected isChangeNicknameDialogOpen = false;
  protected isChangeUsernameDialogOpen = false;
  protected isDeleteAccountDialogOpen = false;

  protected readonly changePasswordForm = new FormGroup({
    currentPassword: new FormControl('', {
      validators: [Validators.required],
      nonNullable: true,
    }),

    newPassword: new FormControl('', {
      validators: [
        Validators.required,
        Validators.minLength(8),
        Validators.maxLength(50),
        passwordPatternValidator,
      ],
      nonNullable: true,
    }),

    retypedNewPassword: new FormControl('', {
      validators: [Validators.required],
      nonNullable: true,
    }),
  });

  protected readonly changeNicknameForm = new FormGroup({
    newNickname: new FormControl('', {
      validators: [
        Validators.required,
        Validators.minLength(4),
        Validators.maxLength(30),
        nicknamePatternValidator,
      ],
      nonNullable: true,
    }),
  });

  protected readonly changeUsernameForm = new FormGroup({
    newUsername: new FormControl('', {
      validators: [
        Validators.required,
        Validators.minLength(8),
        Validators.maxLength(30),
        usernamePatternValidator,
      ],
      nonNullable: true,
    }),

    currentPassword: new FormControl('', {
      validators: [Validators.required],
      nonNullable: true,
    }),
  });

  protected readonly changeAppearanceForm = new FormGroup({
    appearance: new FormControl('', { nonNullable: true }),
  });

  protected readonly deleteUserAccountForm = new FormGroup({
    currentPassword: new FormControl('', {
      validators: [Validators.required],
      nonNullable: true,
    }),
  });

  isChangePasswordFormSubmitting = false;
  isChangeNicknameFormSubmitting = false;
  isChangeUsernameFormSubmitting = false;
  isDeleteAccountFormSubmitting = false;

  ngOnInit(): void {
    const savedTheme = localStorage.getItem('tuiDark');

    this.changeAppearanceForm.controls.appearance.setValue(savedTheme ?? '', {
      emitEvent: false,
    });

    this.changeAppearanceForm.controls.appearance.valueChanges.subscribe((value) => {
      switch (value) {
        case '':
          this.darkMode.reset();
          break;
        case 'true':
          this.darkMode.set(true);
          break;
        case 'false':
          this.darkMode.set(false);
          break;
      }
    });
  }

  protected viewProfileBtn(): void {
    this.router.navigate([`${AppRoutesPagesEnum.PAGE_USER_PROFILE}/${this.user()?.publicUserId}`]);
  }

  protected openChangePasswordDialogBtn(): void {
    this.changePasswordForm.reset();
    this.isChangePasswordDialogOpen = true;
  }

  protected openChangeNicknameDialogBtn(): void {
    this.changeNicknameForm.controls.newNickname.setValue(this.user()?.nickname ?? '');
    this.isChangeNicknameDialogOpen = true;
  }

  protected openChangeUsernameDialogBtn(): void {
    this.changeUsernameForm.markAsUntouched();
    this.changeUsernameForm.controls.newUsername.setValue(this.user()?.username ?? '');
    this.changeUsernameForm.controls.currentPassword.setValue('');
    this.isChangeUsernameDialogOpen = true;
  }

  protected openDeleteUserAccountDialogBtn(): void {
    this.deleteUserAccountForm.reset();
    this.isDeleteAccountDialogOpen = true;
  }

  protected changePassword(): void {
    const currentPassword = this.changePasswordForm.controls.currentPassword.value;
    const newPassword = this.changePasswordForm.controls.newPassword.value;
    const retypedNewPassword = this.changePasswordForm.controls.retypedNewPassword.value;

    if (this.changePasswordForm.invalid) {
      this.changePasswordForm.markAllAsTouched();
      return;
    }

    this.isChangePasswordFormSubmitting = true;

    this.accountService
      .changePassword({
        currentPassword: currentPassword,
        newPassword: newPassword,
        retypedNewPassword: retypedNewPassword,
      })
      .pipe(finalize(() => (this.isChangePasswordFormSubmitting = false)))
      .subscribe({
        next: () => {
          this.isChangePasswordDialogOpen = false;
          this.authService.logout().subscribe({
            next: () => {
              this.router.navigate([AppRoutesPagesEnum.PAGE_LOGIN]);
            },
          });
        },
        error: (error: HttpErrorResponse) => {
          if (error.status === HttpStatusCode.Unauthorized) {
            if (error.error.code === this.requestErrorCode.INCORRECT_PASSWORD) {
              this.changePasswordForm.controls.currentPassword.setErrors({
                [this.requestErrorFormInputIdentifier.incorrectPassword]: true,
              });
            }
          }

          if (error.status === HttpStatusCode.BadRequest) {
            switch (error.error.code) {
              case this.requestErrorCode.NEW_PASSWORD_MUST_BE_DIFFERENT:
                this.changePasswordForm.controls.newPassword.setErrors({
                  [this.requestErrorFormInputIdentifier.newPasswordMustBeDifferent]: true,
                });
                break;

              case this.requestErrorCode.RETYPED_NEW_PASSWORD_DOESNT_MATCH:
                this.changePasswordForm.controls.retypedNewPassword.setErrors({
                  [this.requestErrorFormInputIdentifier.retypedNewPasswordDoesntMatch]: true,
                });
                break;
            }
          }

          if (error.status === 0 || error.status === HttpStatusCode.BadGateway) {
            this.changePasswordForm.setErrors({
              [this.requestErrorFormInputIdentifier.serverUnavailable]: true,
            });
          }
        },
      });
  }

  protected changeNickname(): void {
    const newNickname = this.changeNicknameForm.controls.newNickname.value;

    if (newNickname === this.user()?.nickname) {
      this.isChangeNicknameDialogOpen = false;
      return;
    } else if (this.changeNicknameForm.invalid) {
      this.changeNicknameForm.markAllAsTouched();
      return;
    }

    this.isChangeNicknameFormSubmitting = true;

    this.accountService
      .changeNickname({ newNickname: newNickname })
      .pipe(finalize(() => (this.isChangeNicknameFormSubmitting = false)))
      .subscribe({
        next: () => {
          this.isChangeNicknameDialogOpen = false;
        },
        error: (error: HttpErrorResponse) => {
          if (error.status === HttpStatusCode.Conflict) {
            if (error.error.code === this.requestErrorCode.NICKNAME_TAKEN) {
              this.changeNicknameForm.controls.newNickname.setErrors({
                [this.requestErrorFormInputIdentifier.nicknameTaken]: true,
              });
            }
          } else if (error.status === 0 || error.status === HttpStatusCode.BadGateway) {
            this.changeNicknameForm.setErrors({
              [this.requestErrorFormInputIdentifier.serverUnavailable]: true,
            });
          }
        },
      });
  }

  protected changeUsername(): void {
    const newUsername = this.changeUsernameForm.controls.newUsername.value;
    const currentPassword = this.changeUsernameForm.controls.currentPassword.value;

    if (newUsername === this.user()?.username) {
      this.isChangeUsernameDialogOpen = false;
      return;
    } else if (this.changeUsernameForm.invalid) {
      this.changeUsernameForm.markAllAsTouched();
      return;
    }

    this.isChangeUsernameFormSubmitting = true;

    this.accountService
      .changeUsername({ newUsername: newUsername, currentPassword: currentPassword })
      .pipe(finalize(() => (this.isChangeUsernameFormSubmitting = false)))
      .subscribe({
        next: (updatedUser) => {
          this.isChangeUsernameDialogOpen = false;
          this.authService.logout().subscribe({
            next: () => {
              this.router.navigate([AppRoutesPagesEnum.PAGE_LOGIN]);
            },
          });
        },
        error: (error: HttpErrorResponse) => {
          if (error.status === HttpStatusCode.Conflict) {
            if (error.error.code === this.requestErrorCode.USERNAME_TAKEN) {
              this.changeUsernameForm.controls.newUsername.setErrors({
                [this.requestErrorFormInputIdentifier.usernameTaken]: true,
              });
            }
          } else if (error.status === HttpStatusCode.Unauthorized) {
            if (error.error.code === this.requestErrorCode.INCORRECT_PASSWORD) {
              this.changeUsernameForm.controls.currentPassword.setErrors({
                [this.requestErrorFormInputIdentifier.incorrectPassword]: true,
              });
            }
          } else if (error.status === 0 || error.status === HttpStatusCode.BadGateway) {
            this.changeUsernameForm.setErrors({
              [this.requestErrorFormInputIdentifier.serverUnavailable]: true,
            });
          }
        },
      });
  }

  protected deleteAccount(): void {
    const currentPassword = this.deleteUserAccountForm.controls.currentPassword.value;

    if (this.deleteUserAccountForm.invalid) {
      this.deleteUserAccountForm.markAllAsTouched();
      return;
    }

    this.isDeleteAccountFormSubmitting = true;

    this.accountService
      .deleteAccount({ currentPassword: currentPassword })
      .pipe(finalize(() => (this.isDeleteAccountFormSubmitting = false)))
      .subscribe({
        next: () => {
          this.authService.logout().subscribe({
            next: () => {
              this.router.navigate([AppRoutesPagesEnum.PAGE_LOGIN]);
            },
          });
        },
        error: (error: HttpErrorResponse) => {
          if (error.status === HttpStatusCode.Unauthorized) {
            if (error.error.code === this.requestErrorCode.INCORRECT_PASSWORD) {
              this.deleteUserAccountForm.controls.currentPassword.setErrors({
                [this.requestErrorFormInputIdentifier.incorrectPassword]: true,
              });
            }
          } else if (error.status === 0 || error.status === HttpStatusCode.BadGateway) {
            this.deleteUserAccountForm.setErrors({
              [this.requestErrorFormInputIdentifier.serverUnavailable]: true,
            });
          }
        },
      });
  }
}
