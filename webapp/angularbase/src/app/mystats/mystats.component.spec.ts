import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MystatsComponent } from './mystats.component';

describe('MystatsComponent', () => {
  let component: MystatsComponent;
  let fixture: ComponentFixture<MystatsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MystatsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MystatsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
