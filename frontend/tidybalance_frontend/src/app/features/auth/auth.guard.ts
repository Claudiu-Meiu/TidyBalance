import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './auth.service';
import { UserStateService } from './user-state.service';
import { catchError, map, of, tap } from 'rxjs';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const userState = inject(UserStateService);
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
