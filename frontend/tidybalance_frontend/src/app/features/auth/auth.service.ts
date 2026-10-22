import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Service, signal } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { CurrentUser } from './current-user.model';

interface LoginRequest {
  username: string;
  password: string;
  rememberMe: boolean;
}

interface RegisterRequest {
  nickname: string;
  username: string;
  password: string;
}

@Service()
export class AuthService {
  private http = inject(HttpClient);

  private readonly currentUser = signal<CurrentUser | null>(null);
  readonly user = this.currentUser.asReadonly();

  apiUrl = '/api/users';

  me(): Observable<CurrentUser> {
    return this.http
      .get<CurrentUser>(`${this.apiUrl}/me`)
      .pipe(tap((user) => this.currentUser.set(user)));
  }

  login(request: LoginRequest): Observable<void> {
    const body = new HttpParams()
      .set('username', request.username)
      .set('password', request.password)
      .set('remember-me', request.rememberMe.toString());

    return this.http.post<void>(`${this.apiUrl}/authenticate`, body, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
    });
  }

  register(request: RegisterRequest): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/register`, request);
  }
}
