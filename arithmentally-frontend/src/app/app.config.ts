import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';

//import { provideOAuthClient, AuthConfig } from 'angular-oauth2-oidc';
//import { authConfig } from './config/auth.config'; // create this file below
import { routes } from './app.routes';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
 //   provideOAuthClient(),
//    { provide: AuthConfig, useValue: authConfig } 
 ]
};