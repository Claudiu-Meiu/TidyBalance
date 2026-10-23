import { TestBed } from '@angular/core/testing';
import { ResolveFn } from '@angular/router';
import { publicUserProfileResolver } from './public-user-profile.resolver';
import { PublicUserProfileModel } from './public-user-profile.model';

describe('publicUserProfileResolver', () => {
  const executeResolver: ResolveFn<PublicUserProfileModel> = (...resolverParameters) =>
    TestBed.runInInjectionContext(() => publicUserProfileResolver(...resolverParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeResolver).toBeTruthy();
  });
});
