import { TestBed } from '@angular/core/testing';
import { PublicUserProfileService } from './public-user-profile.service';

describe('PublicUserProfileService', () => {
  let service: PublicUserProfileService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PublicUserProfileService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
