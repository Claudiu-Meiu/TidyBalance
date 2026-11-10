import { Component, inject, input, OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { PublicUserProfileModel } from './public-user-profile.model';
import { UserStateService } from '../auth/user-state.service';
import { TuiButton } from '@taiga-ui/core';
import { Title } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { AppRoutesPagesEnum } from '../../app.routes-pages.enum';

@Component({
  imports: [DatePipe, TuiButton],
  selector: 'app-public-user-profile',
  styleUrl: './public-user-profile.css',
  templateUrl: './public-user-profile.html',
})
export class PublicUserProfile implements OnInit {
  private router = inject(Router);
  private titleService = inject(Title);
  private readonly userState = inject(UserStateService);

  user = this.userState.user();

  publicUserProfile = input.required<PublicUserProfileModel>();

  ngOnInit(): void {
    this.titleService.setTitle(`Profile - ${this.publicUserProfile().nickname}`);
  }

  isOwnPublicProfile(): boolean {
    return true ? this.user?.publicUserId === this.publicUserProfile().publicUserId : false;
  }

  editProfileBtn() {
    this.router.navigate([AppRoutesPagesEnum.PAGE_ACCOUNT]);
  }
}
