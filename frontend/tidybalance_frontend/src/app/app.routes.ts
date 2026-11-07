import { Routes } from '@angular/router';

import { Login } from './features/auth/login/login';
import { Home } from './features/home/home';
import { Register } from './features/account-management/register/register';
import { authGuard } from './features/auth/auth.guard';
import { MainLayout } from './layouts/main-layout/main-layout';
import { PageNotFound } from './features/page-not-found/page-not-found';
import { PublicUserProfile } from './features/public-user-profile/public-user-profile';
import { publicUserProfileResolver } from './features/public-user-profile/public-user-profile.resolver';
import { Account } from './features/account-management/account/account';

export const routes: Routes = [
  {
    path: '',
    component: MainLayout,
    canActivate: [authGuard],
    children: [
      {
        path: '',
        pathMatch: 'full',
        redirectTo: 'home',
      },
      {
        path: 'user/profile/:publicUserId',
        component: PublicUserProfile,
        resolve: {
          publicUserProfile: publicUserProfileResolver,
        },
      },
      {
        path: 'account',
        component: Account,
        title: 'Account',
      },
      {
        path: 'home',
        component: Home,
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
