import { TestBed } from '@angular/core/testing';

import { FirstLoginStudentAccountService } from './first-login-student-account.service';

describe('FirstLoginStudentAccountService', () => {
  let service: FirstLoginStudentAccountService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FirstLoginStudentAccountService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
