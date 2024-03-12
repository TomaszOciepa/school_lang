import { TestBed } from '@angular/core/testing';

import { LoadUserProfileService } from './load-user-profile.service';

describe('LoadUserProfileService', () => {
  let service: LoadUserProfileService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LoadUserProfileService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
