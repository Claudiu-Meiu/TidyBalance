import { Component, inject } from '@angular/core';
import { UserState } from '../../../auth/user-state';

@Component({
  imports: [],
  selector: 'app-home',
  styleUrl: './home.css',
  templateUrl: './home.html',
})
export class Home {
  userState = inject(UserState);
}
