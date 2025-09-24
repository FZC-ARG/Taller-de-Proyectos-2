import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InicioDocentesComponent } from './inicio-docentes.component';

describe('InicioDocentesComponent', () => {
  let component: InicioDocentesComponent;
  let fixture: ComponentFixture<InicioDocentesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InicioDocentesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InicioDocentesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
