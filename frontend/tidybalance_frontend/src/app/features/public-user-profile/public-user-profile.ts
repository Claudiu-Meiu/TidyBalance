import { Component, inject, input, linkedSignal, OnInit, signal } from '@angular/core';
import { FormGroup, FormControl, ReactiveFormsModule, Validators } from '@angular/forms';
import { DatePipe } from '@angular/common';
import { Router } from '@angular/router';
import { UserStateService } from '../auth/user-state.service';
import { PublicUserProfileModel } from './public-user-profile.model';
import { AppRoutesPagesEnum } from '../../app.routes-pages.enum';

import { TuiButton, TuiDialog, TuiIcon, TuiTitle } from '@taiga-ui/core';
import { TuiCardLarge, TuiHeader, TuiForm } from '@taiga-ui/layout';
import { Title } from '@angular/platform-browser';
import { TuiAvatar, TuiFiles } from '@taiga-ui/kit';
import { PublicUserProfileService } from './public-user-profile.service';
import { toSignal } from '@angular/core/rxjs-interop';

@Component({
  imports: [
    ReactiveFormsModule,
    DatePipe,
    TuiButton,
    TuiCardLarge,
    TuiHeader,
    TuiTitle,
    TuiAvatar,
    TuiFiles,
    TuiDialog,
    TuiIcon,
  ],
  selector: 'app-public-user-profile',
  styleUrl: './public-user-profile.css',
  templateUrl: './public-user-profile.html',
})
export class PublicUserProfile implements OnInit {
  private readonly router = inject(Router);
  private readonly titleService = inject(Title);
  private readonly userState = inject(UserStateService);
  protected readonly publicUserProfileService = inject(PublicUserProfileService);

  protected readonly user = this.userState.user;

  protected readonly publicUserProfileInput = input.required<PublicUserProfileModel>({
    alias: 'publicUserProfile',
  });

  protected readonly publicUserProfile = linkedSignal(() => this.publicUserProfileInput());

  protected isUpdateProfilePictureDialogOpen = false;

  protected readonly profilePictureForm = new FormGroup({
    profilePictureFile: new FormControl<File | ''>('', {
      validators: [Validators.required],
      nonNullable: true,
    }),
  });

  protected readonly loadedProfilePictureFile = toSignal(
    this.profilePictureForm.controls.profilePictureFile.valueChanges,
    { initialValue: this.profilePictureForm.controls.profilePictureFile.value },
  );

  protected readonly profilePictureVersion = signal(Date.now());

  ngOnInit(): void {
    this.titleService.setTitle(`Profile - ${this.publicUserProfile().nickname}`);
  }

  protected openUpdateProfilePictureDialogBtn(): void {
    this.profilePictureForm.reset();
    this.isUpdateProfilePictureDialogOpen = true;
  }

  protected uploadProfilePicture(): void {
    const profilePicture = this.profilePictureForm.controls.profilePictureFile.value;

    if (this.profilePictureForm.invalid) {
      this.profilePictureForm.markAllAsTouched();
      return;
    }

    if (!profilePicture) return;

    this.publicUserProfileService.updateProfilePicture(profilePicture).subscribe({
      next: () => {
        this.isUpdateProfilePictureDialogOpen = false;

        const currentPublicUserProfile = this.publicUserProfile();
        const extension = profilePicture.name.substring(profilePicture.name.lastIndexOf('.'));
        const newFilename = `${currentPublicUserProfile.publicUserId}${extension}`;

        this.publicUserProfile.set({
          ...currentPublicUserProfile,
          profilePicture: newFilename,
        });

        this.profilePictureVersion.set(Date.now());

        this.profilePictureForm.reset();
      },
      error: (err) => {
        console.error('Error:', err);
      },
    });
  }

  protected isOwnPublicProfile(): boolean {
    return true ? this.user()?.publicUserId === this.publicUserProfile().publicUserId : false;
  }

  protected accountSettingsBtn(): void {
    this.router.navigate([AppRoutesPagesEnum.PAGE_ACCOUNT]);
  }

  protected removeProfilePictureFileBtn(): void {
    this.profilePictureForm.controls.profilePictureFile.setValue('');
  }
}
