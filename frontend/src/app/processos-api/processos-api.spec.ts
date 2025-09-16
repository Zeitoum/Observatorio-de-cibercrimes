import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProcessosApi } from './processos-api';

describe('ProcessosApi', () => {
  let component: ProcessosApi;
  let fixture: ComponentFixture<ProcessosApi>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProcessosApi]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProcessosApi);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
