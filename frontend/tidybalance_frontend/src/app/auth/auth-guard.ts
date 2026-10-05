import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { Auth } from './auth';
import { UserState } from './user-state';
import { catchError, map, of, tap } from 'rxjs';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(Auth);
  const userState = inject(UserState);
  const router = inject(Router);

  return authService.me().pipe(
    tap((user) => userState.setUser(user)),
    map(() => true),
    catchError(() => {
      userState.clearUser();
      return of(router.createUrlTree(['/login']));
    }),
  );
};
