import { Service, signal } from '@angular/core';
import { CurrentUser } from './current-user.model';

@Service()
export class UserStateService {
  private readonly currentUser = signal<CurrentUser | null>(null);

  readonly user = this.currentUser.asReadonly();

  setUser(user: CurrentUser): void {
    this.currentUser.set(user);
  }

  clearUser(): void {
    this.currentUser.set(null);
  }
}
