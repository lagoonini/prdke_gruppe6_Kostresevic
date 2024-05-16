import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DriversLogViewComponent } from './drivers-log-view.component';

describe('DriversLogViewComponent', () => {
  let component: DriversLogViewComponent;
  let fixture: ComponentFixture<DriversLogViewComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DriversLogViewComponent]
    });
    fixture = TestBed.createComponent(DriversLogViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
