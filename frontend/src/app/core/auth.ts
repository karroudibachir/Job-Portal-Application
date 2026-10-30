import { HttpClient } from '@angular/common/http';
import { Injectable, inject, signal, computed } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
export interface User {id:number;email:string;fullName:string;roles:string[]}
interface AuthReply {accessToken:string;expiresIn:number;user:User}
@Injectable({providedIn:'root'}) export class AuthService {
 private http=inject(HttpClient); private router=inject(Router);
 readonly currentUser=signal<User|null>(this.readUser()); readonly isAuthenticated=computed(()=>this.currentUser()!==null); readonly roles=computed(()=>this.currentUser()?.roles??[]);
 private readUser():User|null {try{const raw=localStorage.getItem('user');return raw?JSON.parse(raw) as User:null}catch{return null}}
 login(email:string,password:string):Observable<AuthReply>{return this.http.post<AuthReply>('/api/auth/login',{email,password}).pipe(tap(r=>this.store(r)))}
 register(fullName:string,email:string,password:string,role:string):Observable<AuthReply>{return this.http.post<AuthReply>('/api/auth/register',{fullName,email,password,role}).pipe(tap(r=>this.store(r)))}
 private store(r:AuthReply):void {localStorage.setItem('accessToken',r.accessToken);localStorage.setItem('user',JSON.stringify(r.user));this.currentUser.set(r.user)}
 logout():void {localStorage.removeItem('accessToken');localStorage.removeItem('user');this.currentUser.set(null);void this.router.navigateByUrl('/login')}
 dashboardPath():string {const roles=this.roles();return roles.includes('ADMIN')?'/admin':roles.includes('EMPLOYER')?'/employer/dashboard':'/seeker/dashboard'}
}
export const authGuard:CanActivateFn=()=>{const auth=inject(AuthService);const router=inject(Router);return auth.isAuthenticated()?true:router.createUrlTree(['/login'])};
export const roleGuard=(role:string):CanActivateFn=>()=>{const auth=inject(AuthService);const router=inject(Router);return auth.roles().includes(role)?true:router.createUrlTree([auth.isAuthenticated()?auth.dashboardPath():'/login'])};
