import { Component, inject } from '@angular/core';
import { UserStateService } from '../auth/user-state.service';

@Component({
  imports: [],
  selector: 'app-home',
  styleUrl: './home.css',
  templateUrl: './home.html',
})
export class Home {
  userState = inject(UserStateService);
}
