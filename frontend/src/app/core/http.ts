import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { AuthService } from './auth';
export const authInterceptor:HttpInterceptorFn=(req,next)=>{const auth=inject(AuthService);const token=localStorage.getItem('accessToken');const request=token?req.clone({setHeaders:{Authorization:`Bearer ${token}`}}):req;return next(request).pipe(catchError(error=>{if(error.status===401&&auth.isAuthenticated())auth.logout();return throwError(()=>error)}))};
