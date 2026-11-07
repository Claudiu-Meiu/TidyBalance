import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Service, signal } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { CurrentUser } from './current-user.model';
import { AppRoutesApiEnum } from '../../app.routes-api.enum';

interface LoginRequest {
  username: string;
  password: string;
  rememberMe: boolean;
}

@Service()
export class AuthService {
  private http = inject(HttpClient);

  private readonly currentUser = signal<CurrentUser | null>(null);
  readonly user = this.currentUser.asReadonly();

  me(): Observable<CurrentUser> {
    return this.http
      .get<CurrentUser>(AppRoutesApiEnum.API_USERS_ACCOUNT)
      .pipe(tap((user) => this.currentUser.set(user)));
  }

  login(request: LoginRequest): Observable<void> {
    const body = new HttpParams()
      .set('username', request.username)
      .set('password', request.password)
      .set('remember-me', request.rememberMe.toString());

    return this.http.post<void>(AppRoutesApiEnum.API_USERS_AUTHENTICATE, body, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
    });
  }

  logout(): Observable<void> {
    return this.http.post<void>(AppRoutesApiEnum.API_USERS_LOGOUT, {});
  }
}
