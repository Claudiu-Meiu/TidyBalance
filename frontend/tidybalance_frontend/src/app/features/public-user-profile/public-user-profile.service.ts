import { HttpClient } from '@angular/common/http';
import { inject, Service } from '@angular/core';
import { Observable } from 'rxjs';

import { PublicUserProfileModel } from './public-user-profile.model';

@Service()
export class PublicUserProfileService {
  private http = inject(HttpClient);

  private readonly apiUrl = '/api/users/profiles';

  publicUserProfile(publicUserId: string): Observable<PublicUserProfileModel> {
    return this.http.get<PublicUserProfileModel>(`${this.apiUrl}/${publicUserId}`);
  }
}
