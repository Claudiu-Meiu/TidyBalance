import { Component, input } from '@angular/core';
import { PublicUserProfileModel } from './public-user-profile.model';

@Component({
  selector: 'app-public-user-profile',
  templateUrl: './public-user-profile.html',
  styleUrl: './public-user-profile.css',
})
export class PublicUserProfile {
  publicUserProfile = input.required<PublicUserProfileModel>();
}
