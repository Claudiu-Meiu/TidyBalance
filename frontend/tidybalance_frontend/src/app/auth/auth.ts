import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Service, signal } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { CurrentUser } from './current-user';

interface LoginRequest {
  username: string;
  password: string;
}

@Service()
export class Auth {
  private http = inject(HttpClient);

  private readonly currentUser = signal<CurrentUser | null>(null);
  readonly user = this.currentUser.asReadonly();

  apiUrl = 'http://localhost:8080';

  login(credentials: LoginRequest): Observable<void> {
    const body = new HttpParams()
      .set('username', credentials.username)
      .set('password', credentials.password);

    return this.http.post<void>(`${this.apiUrl}/authenticate`, body, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
    });
  }

  me(): Observable<CurrentUser> {
    return this.http
      .get<CurrentUser>(`${this.apiUrl}/users/me`)
      .pipe(tap((user) => this.currentUser.set(user)));
  }
}
