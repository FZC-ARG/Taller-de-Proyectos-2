import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TestGardnerComponent } from './test-gardner.component';

describe('TestGardnerComponent', () => {
  let component: TestGardnerComponent;
  let fixture: ComponentFixture<TestGardnerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TestGardnerComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TestGardnerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
