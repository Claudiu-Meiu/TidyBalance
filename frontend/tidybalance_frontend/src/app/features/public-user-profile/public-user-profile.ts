import { Component, inject, input } from '@angular/core';
import { PublicUserProfileModel } from './public-user-profile.model';
import { UserStateService } from '../auth/user-state.service';
import { TuiButton } from '@taiga-ui/core';

@Component({
  imports: [TuiButton],
  selector: 'app-public-user-profile',
  styleUrl: './public-user-profile.css',
  templateUrl: './public-user-profile.html',
})
export class PublicUserProfile {
  private readonly userState = inject(UserStateService);

  user = this.userState.user();

  publicUserProfile = input.required<PublicUserProfileModel>();

  isOwnPublicProfile(): boolean {
    return true ? this.user?.publicUserId === this.publicUserProfile().publicUserId : false;
  }
}
