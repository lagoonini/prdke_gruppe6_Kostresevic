import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DriversLogListComponent } from './drivers-log-list.component';

describe('DriversLogListComponent', () => {
  let component: DriversLogListComponent;
  let fixture: ComponentFixture<DriversLogListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DriversLogListComponent]
    });
    fixture = TestBed.createComponent(DriversLogListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
