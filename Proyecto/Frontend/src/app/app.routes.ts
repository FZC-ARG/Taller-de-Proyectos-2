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
];
