import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';


export const routes: Routes = [
    {
        path: '',
        redirectTo: 'login',
        pathMatch: 'full'
    },

    {
        path: 'login',
        loadComponent: () => import('./login/login.component').then(m => m.LoginComponent)
    },
    {
        path: 'inicio',
        loadComponent: () => import('./inicio/inicio.component').then(m => m.InicioComponent)     
    },
    {
        path: 'inicio-docentes',
        loadComponent: () => import('./inicio-docentes/inicio-docentes.component').then(m => m.InicioDocentesComponent)     
    },
    {
        path: 'inicio-alumnos',
        loadComponent: () => import('./inicio-alumnos/inicio-alumnos.component').then(m => m.InicioAlumnosComponent)     
    }
];
