import { Routes } from '@angular/router';

export const routes: Routes = [
  // 1. Redirect empty path to login
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  
  // 2. Add the login route
  { 
    path: 'login', 
    loadComponent: () => import('./login/login').then(m => m.Login) 
  },
  
  // 3. Keep your existing lazy-loaded routes
  { 
    path: 'consumer', 
    loadComponent: () => import('./consumer/consumer').then(m => m.Consumer) 
  },
  { 
    path: 'admin', 
    loadComponent: () => import('./admin/admin').then(m => m.Admin) 
  },
  { 
    path: 'seller', 
    loadComponent: () => import('./seller/seller').then(m => m.Seller) 
  }
];