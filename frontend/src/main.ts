import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { bootstrapApplication } from '@angular/platform-browser';
import { provideRouter, Routes } from '@angular/router';
import { AppComponent } from './app/app.component';
import { authGuard, roleGuard } from './app/core/auth';
const routes: Routes = [
 {path:'',loadComponent:()=>import('./app/pages').then(m=>m.HomeComponent)}, {path:'jobs',loadComponent:()=>import('./app/pages').then(m=>m.JobsComponent)},
 {path:'jobs/:id',loadComponent:()=>import('./app/pages').then(m=>m.JobDetailComponent)},
 {path:'login',loadComponent:()=>import('./app/pages').then(m=>m.LoginComponent)}, {path:'register',loadComponent:()=>import('./app/pages').then(m=>m.RegisterComponent)},
 {path:'seeker/dashboard',loadComponent:()=>import('./app/pages').then(m=>m.DashboardComponent),canActivate:[authGuard,roleGuard('JOB_SEEKER')]},
 {path:'employer/jobs/new',loadComponent:()=>import('./app/pages').then(m=>m.CreateJobComponent),canActivate:[authGuard,roleGuard('EMPLOYER')]},
 {path:'employer/dashboard',loadComponent:()=>import('./app/pages').then(m=>m.DashboardComponent),canActivate:[authGuard,roleGuard('EMPLOYER')]},
 {path:'admin',loadComponent:()=>import('./app/pages').then(m=>m.AdminDashboardComponent),canActivate:[authGuard,roleGuard('ADMIN')]},
 {path:'**',redirectTo:''}
];
import { authInterceptor } from './app/core/http';
bootstrapApplication(AppComponent,{providers:[provideRouter(routes),provideHttpClient(withInterceptors([authInterceptor]))]}).catch(console.error);
