import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideRouter } from '@angular/router';
import { AuthService } from './auth';

describe('AuthService', () => {
  beforeEach(() => { localStorage.clear(); TestBed.configureTestingModule({providers:[provideHttpClient(),provideRouter([])]}); });
  it('starts unauthenticated', () => { expect(TestBed.inject(AuthService).isAuthenticated()).toBeFalse(); });
  it('restores the current user and dashboard role from storage', () => {
    localStorage.setItem('user',JSON.stringify({id:7,email:'seeker@example.com',fullName:'A Seeker',roles:['JOB_SEEKER']}));
    const auth=TestBed.inject(AuthService);
    expect(auth.isAuthenticated()).toBeTrue();
    expect(auth.dashboardPath()).toBe('/seeker/dashboard');
  });
});
