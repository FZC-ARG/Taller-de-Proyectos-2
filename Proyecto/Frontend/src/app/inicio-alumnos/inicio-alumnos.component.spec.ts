import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InicioAlumnosComponent } from './inicio-alumnos.component';

describe('InicioAlumnosComponent', () => {
  let component: InicioAlumnosComponent;
  let fixture: ComponentFixture<InicioAlumnosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InicioAlumnosComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InicioAlumnosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
