import { Component, inject } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
  FormsModule,
} from '@angular/forms';
import { Router } from '@angular/router';
import { TuiButton, TuiInput, TuiError, TuiTitle, TuiIcon } from '@taiga-ui/core';
import { TuiPassword } from '@taiga-ui/kit';
import { TuiCardLarge, TuiForm, TuiHeader } from '@taiga-ui/layout';
import { Auth } from '../auth';

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
})
export class Login {
  private authService = inject(Auth);
  private router = inject(Router);

  protected readonly loginForm = new FormGroup({
    username: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required),
  });

  login() {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    this.authService
      .login({
        username: this.loginForm.value.username!,
        password: this.loginForm.value.password!,
      })
      .subscribe({
        next: () => {
          this.router.navigate(['/home']);
        },
        error: () => {
          this.loginForm.setErrors({
            invalidCredentials: true,
          });
        },
      });
  }
}
