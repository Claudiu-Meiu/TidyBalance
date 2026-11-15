import { AbstractControl, ValidationErrors } from '@angular/forms';
import { TUI_VALIDATION_ERRORS } from '@taiga-ui/core';

export enum RequestErrorCode {
  INCORRECT_PASSWORD = 'INCORRECT_PASSWORD',
  NEW_PASSWORD_MUST_BE_DIFFERENT = 'NEW_PASSWORD_MUST_BE_DIFFERENT',
  RETYPED_NEW_PASSWORD_DOESNT_MATCH = 'RETYPED_NEW_PASSWORD_DOESNT_MATCH',
  NICKNAME_TAKEN = 'NICKNAME_TAKEN',
  USERNAME_TAKEN = 'USERNAME_TAKEN',
}

export enum RequestErrorFormInputIdentifier {
  invalidLogin = 'invalidLogin',
  incorrectPassword = 'incorrectPassword',
  newPasswordMustBeDifferent = 'newPasswordMustBeDifferent',
  retypedNewPasswordDoesntMatch = 'retypedNewPasswordDoesntMatch',
  nicknameTaken = 'nicknameTaken',
  usernameTaken = 'usernameTaken',
  serverUnavailable = 'serverUnavailable',
}

// Custom Global Form Validation Errors Provider
export const provideCustomGlobalFormValidationErrors = () => ({
  provide: TUI_VALIDATION_ERRORS,
  useValue: {
    required: 'This field is required.',
    minlength: ({ requiredLength }: { requiredLength: number }) =>
      `Must be at least ${requiredLength} characters long.`,
    maxlength: ({ requiredLength }: { requiredLength: number }) =>
      `Must not exceed ${requiredLength} characters.`,
    [RequestErrorFormInputIdentifier.incorrectPassword]: 'Password is incorrect.',
    [RequestErrorFormInputIdentifier.newPasswordMustBeDifferent]:
      'New password must be different from the current password.',
    [RequestErrorFormInputIdentifier.retypedNewPasswordDoesntMatch]:
      "Retyped new password doesn't match.",
    [RequestErrorFormInputIdentifier.nicknameTaken]: 'Nickname is already taken.',
    [RequestErrorFormInputIdentifier.usernameTaken]: 'Username is already taken.',
  },
});

// Form Input Pattern Validators
export function passwordPatternValidator(control: AbstractControl): ValidationErrors | null {
  const password: string = control.value ?? '';
  const message =
    'Password must contain at least one uppercase letter, one lowercase letter, one number, one special character, and no whitespaces.';
  const pattern: RegExp = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z\d\s])\S+$/;

  return pattern.test(password) ? null : { other: message };
}

export function nicknamePatternValidator(control: AbstractControl): ValidationErrors | null {
  const nickname: string = control.value ?? '';
  const message = 'Nickname may contain only one space between words.';
  const pattern: RegExp = /^\S+(?: \S+)*$/;

  return pattern.test(nickname) ? null : { other: message };
}

export function usernamePatternValidator(control: AbstractControl): ValidationErrors | null {
  const username: string = control.value ?? '';
  const message = 'Username must not contain whitespaces.';
  const pattern: RegExp = /^\S+$/;

  return pattern.test(username) ? null : { other: message };
}
