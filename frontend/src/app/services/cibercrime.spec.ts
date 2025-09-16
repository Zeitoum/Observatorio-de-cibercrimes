import { TestBed } from '@angular/core/testing';

import { CibercrimeService } from './cibercrimeService';

describe('Cibercrime', () => {
  let service: CibercrimeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CibercrimeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
