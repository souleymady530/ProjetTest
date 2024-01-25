import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AccueiComponent } from './accuei.component';

describe('AccueiComponent', () => {
  let component: AccueiComponent;
  let fixture: ComponentFixture<AccueiComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AccueiComponent]
    });
    fixture = TestBed.createComponent(AccueiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
