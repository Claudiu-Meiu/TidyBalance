import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RedirectCommand, ResolveFn, Router } from '@angular/router';

import { PublicUserProfileModel } from './public-user-profile.model';
import { PublicUserProfileService } from './public-user-profile.service';
import { catchError, of } from 'rxjs';

export const publicUserProfileResolver: ResolveFn<PublicUserProfileModel> = (
  route: ActivatedRouteSnapshot,
) => {
  const router = inject(Router);
  const publicUserProfileService = inject(PublicUserProfileService);
  const publicUserId = route.paramMap.get('publicUserId')!;

  return publicUserProfileService
    .publicUserProfile(publicUserId)
    .pipe(catchError(() => of(new RedirectCommand(router.parseUrl('/not-found')))));
};
