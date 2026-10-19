import { Routes } from '@angular/router';

import { Login } from './views/pages/login/login';
import { Home } from './views/pages/home/home';
import { Register } from './views/pages/register/register';
import { authGuard } from './auth/auth-guard';
import { MainLayout } from './views/layouts/main-layout/main-layout';
import { PageNotFound } from './views/pages/page-not-found/page-not-found';

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
