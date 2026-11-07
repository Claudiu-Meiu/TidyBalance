import { HttpClient } from '@angular/common/http';
import { inject, Service } from '@angular/core';
import { Observable } from 'rxjs';
import { AppRoutesApiEnum } from '../../app.routes-api.enum';
import { CurrentUser } from '../auth/current-user.model';

interface RegisterRequest {
  nickname: string;
  username: string;
  password: string;
}

interface ChangeUserPasswordRequest {
  currentPassword: string;
  newPassword: string;
  retypedNewPassword: string;
}

interface ChangeUserNicknameRequest {
  newNickname: string;
}

interface ChangeUserUsernameRequest {
  newUsername: string;
  currentPassword: string;
}

@Service()
export class AccountService {
  private http = inject(HttpClient);

  register(request: RegisterRequest): Observable<void> {
    return this.http.post<void>(AppRoutesApiEnum.API_USERS_ACCOUNT_REGISTER, request);
  }

  changePassword(request: ChangeUserPasswordRequest): Observable<void> {
    return this.http.patch<void>(AppRoutesApiEnum.API_USERS_ACCOUNT_PASSWORD, request);
  }

  changeNickname(request: ChangeUserNicknameRequest): Observable<CurrentUser> {
    return this.http.patch<CurrentUser>(AppRoutesApiEnum.API_USERS_ACCOUNT_NICKNAME, request);
  }

  changeUsername(request: ChangeUserUsernameRequest): Observable<void> {
    return this.http.patch<void>(AppRoutesApiEnum.API_USERS_ACCOUNT_USERNAME, request);
  }

  deleteAccount(): Observable<void> {
    return this.http.delete<void>(AppRoutesApiEnum.API_USERS_ACCOUNT);
  }
}
