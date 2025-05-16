import { ApplicationConfig, importProvidersFrom, inject, provideEnvironmentInitializer, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import { OAuthModule, AuthConfig, OAuthStorage, provideOAuthClient } from 'angular-oauth2-oidc';
import { MatMomentDateModule } from '@angular/material-moment-adapter';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { HTTP_INTERCEPTORS, HttpClient, provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { HttpXSRFInterceptor } from './interceptor/http.csrf.interceptor';
import { AppAuthService } from './service/app.auth.service';
import { authConfig } from './app.auth';


export function storageFactory(): OAuthStorage {
  return sessionStorage;
}

// Translate loader factory
export function HttpLoaderFactory(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient);
}

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(withInterceptorsFromDi()),
    provideOAuthClient(),
    { provide: AuthConfig, useValue: authConfig },
    { provide: OAuthStorage, useFactory: storageFactory },
    { provide: HTTP_INTERCEPTORS, useClass: HttpXSRFInterceptor, multi: true },
    importProvidersFrom(
      OAuthModule.forRoot({ resourceServer: { sendAccessToken: true } }),
      TranslateModule.forRoot({
        loader: {
          provide: TranslateLoader,
          useFactory: HttpLoaderFactory,
          deps: [HttpClient],
        },
      }),
      MatMomentDateModule
    ),
    provideEnvironmentInitializer(() => {inject(AppAuthService).initAuth().finally()})
  ],
};

