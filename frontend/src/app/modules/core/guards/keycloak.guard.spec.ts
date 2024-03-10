import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { keycloakGuard } from './authguard.guard';

describe('keycloakGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) =>
    TestBed.runInInjectionContext(() => keycloakGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
