import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ActividadUsuariosComponent } from './actividad-usuarios.component';

describe('ActividadUsuariosComponent', () => {
  let component: ActividadUsuariosComponent;
  let fixture: ComponentFixture<ActividadUsuariosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ActividadUsuariosComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ActividadUsuariosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
