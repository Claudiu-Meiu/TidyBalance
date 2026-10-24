import { Routes } from '@angular/router';

import { Login } from './features/auth/login/login';
import { Home } from './features/home/home';
import { Register } from './features/auth/register/register';
import { authGuard } from './features/auth/auth.guard';
import { MainLayout } from './layouts/main-layout/main-layout';
import { PageNotFound } from './features/page-not-found/page-not-found';
import { PublicUserProfile } from './features/public-user-profile/public-user-profile';
import { publicUserProfileResolver } from './features/public-user-profile/public-user-profile.resolver';

export const routes: Routes = [
  {
    path: '',
    component: MainLayout,
    children: [
      {
        path: '',
        pathMatch: 'full',
        redirectTo: 'home',
      },
      {
        path: 'user/profile/:publicUserId',
        component: PublicUserProfile,
        canActivate: [authGuard],
        resolve: {
          publicUserProfile: publicUserProfileResolver,
        },
      },
      {
        path: 'home',
        component: Home,
        canActivate: [authGuard],
        title: 'Home',
      },
    ],
  },
  {
    path: 'login',
    component: Login,
    title: 'Login',
  },
  {
    path: 'register',
    component: Register,
    title: 'Register',
  },
  {
    path: '**',
    component: PageNotFound,
  },
];
