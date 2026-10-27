import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RedirectCommand, ResolveFn, Router } from '@angular/router';

import { PublicUserProfileModel } from './public-user-profile.model';
import { PublicUserProfileService } from './public-user-profile.service';
import { catchError, of } from 'rxjs';
import { AppRoutesPagesEnum } from '../../app.routes-pages.enum';

export const publicUserProfileResolver: ResolveFn<PublicUserProfileModel> = (
  route: ActivatedRouteSnapshot,
) => {
  const router = inject(Router);
  const publicUserProfileService = inject(PublicUserProfileService);
  const publicUserId = route.paramMap.get('publicUserId')!;

  return publicUserProfileService
    .publicUserProfile(publicUserId)
    .pipe(
      catchError(() => of(new RedirectCommand(router.parseUrl(AppRoutesPagesEnum.PAGE_NOT_FOUND)))),
    );
};
