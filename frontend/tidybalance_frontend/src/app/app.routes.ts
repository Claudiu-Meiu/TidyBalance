import { Routes } from '@angular/router';
import { Login } from './views/pages/login/login';
import { Home } from './views/pages/home/home';
import { authGuard } from './auth/auth-guard';
import { Register } from './views/pages/register/register';

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'home',
  },
  {
    path: 'home',
    component: Home,
    canActivate: [authGuard],
  },
  {
    path: 'login',
    component: Login,
  },
  {
    path: 'register',
    component: Register,
  },
];
