import { ApplicationConfig, importProvidersFrom, inject, provideEnvironmentInitializer, provideZoneChangeDetection } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';

import { AuthConfig, OAuthModule, OAuthStorage, provideOAuthClient } from 'angular-oauth2-oidc';

import { authConfig } from './app.auth';
import { AppAuthService } from './service/app.auth.service';

export function storageFactory(): OAuthStorage {
  return sessionStorage;
}

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(withInterceptorsFromDi()),

    importProvidersFrom(
      BrowserModule,
      OAuthModule.forRoot({
        resourceServer: {
          sendAccessToken: true,
        },
      }),
    ),

    { provide: AuthConfig, useValue: authConfig },
    { provide: OAuthStorage, useFactory: storageFactory },

    provideOAuthClient(),

    provideEnvironmentInitializer(() =>
      inject(AppAuthService).initAuth().finally(() => {})
    ),
  ],
};
