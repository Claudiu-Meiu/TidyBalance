import { HttpClient } from '@angular/common/http';
import { inject, Service } from '@angular/core';
import { Observable } from 'rxjs';

import { PublicUserProfileModel } from './public-user-profile.model';
import { AppRoutesApiEnum } from '../../app.routes-api.enum';

@Service()
export class PublicUserProfileService {
  private http = inject(HttpClient);

  publicUserProfile(publicUserId: string): Observable<PublicUserProfileModel> {
    return this.http.get<PublicUserProfileModel>(
      `${AppRoutesApiEnum.API_USERS_PROFILES}/${publicUserId}`,
    );
  }

  getProfilePictureUrl(profilePictureFilename: string): string {
    return `${AppRoutesApiEnum.API_USERS_PROFILES_PROFILE_PICTURE}/${profilePictureFilename}`;
  }

  updateProfilePicture(file: File): Observable<void> {
    const formData = new FormData();

    formData.append('file', file, file.name);

    return this.http.post<void>(AppRoutesApiEnum.API_USERS_PROFILES_PROFILE_PICTURE, formData);
  }

  removeProfilePicture(): Observable<void> {
    return this.http.delete<void>(AppRoutesApiEnum.API_USERS_PROFILES_PROFILE_PICTURE);
  }
}
