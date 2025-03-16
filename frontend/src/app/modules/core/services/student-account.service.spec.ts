import { TestBed } from '@angular/core/testing';

import { StudentAccountService } from './student-account.service';

describe('StudentAccountService', () => {
  let service: StudentAccountService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StudentAccountService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
