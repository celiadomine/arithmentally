import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';
import { AuthConfig, OAuthErrorEvent, OAuthService } from 'angular-oauth2-oidc';
import { BehaviorSubject, Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AppAuthService {
  private jwtHelper: JwtHelperService = new JwtHelperService();

  private usernameSubject = new BehaviorSubject<string>('');
  public readonly usernameObservable = this.usernameSubject.asObservable();

  private useraliasSubject = new BehaviorSubject<string>('');
  public readonly useraliasObservable = this.useraliasSubject.asObservable();

  private accessTokenSubject = new BehaviorSubject<string>('');
  public readonly accessTokenObservable = this.accessTokenSubject.asObservable();

  private _decodedAccessToken: Record<string, unknown> | null = null;
  private _accessToken = '';

  constructor(
    private oauthService: OAuthService,
    private authConfig: AuthConfig
  ) {
    this.handleEvents(null);
  }

  get decodedAccessToken(): Record<string, unknown> | null {
    return this._decodedAccessToken;
  }

  get accessToken(): string {
    return this._accessToken;
  }

  async initAuth(): Promise<void> {
    return new Promise<void>((resolve) => {
      this.oauthService.configure(this.authConfig);
      this.oauthService.events.subscribe((e: unknown) => this.handleEvents(e));

      this.oauthService.loadDiscoveryDocumentAndTryLogin().then(() => {
        if (this.oauthService.hasValidAccessToken()) {
          if (!sessionStorage.getItem('reloaded')) {
            sessionStorage.setItem('reloaded', 'true');
            window.location.reload();
          }
        }
        resolve();
      });

      this.oauthService.setupAutomaticSilentRefresh();
    });
  }

  public getRoles(): Observable<string[]> {
    if (this._decodedAccessToken !== null) {
      return new Observable<string[]>((observer) => {
        const access = this._decodedAccessToken?.['resource_access'] as Record<string, unknown>;
        const client = access?.['arithmentally'] as { roles?: unknown };
        const clientRoles = client?.roles;


        if (Array.isArray(clientRoles)) {
          observer.next(clientRoles.map((r: string) => r.replace('ROLE_', '')));
        } else if (typeof clientRoles === 'string') {
          observer.next([clientRoles.replace('ROLE_', '')]);
        } else {
          observer.next([]);
        }
      });
    }
    return of([]);
  }

  public getIdentityClaims(): Record<string, unknown> | null {
    return this.oauthService.getIdentityClaims() as Record<string, unknown> | null;
  }

  public isAuthenticated(): boolean {
    return this.oauthService.hasValidAccessToken();
  }

  public logout(): void {
    this.oauthService.logOut();
    this.useraliasSubject.next('');
    this.usernameSubject.next('');
  }

  public login(): void {
    this.oauthService.initLoginFlow();
  }

  private handleEvents(event: unknown): void {
    if (event instanceof OAuthErrorEvent) {
      // Log errors if needed
      return;
    }

    this._accessToken = this.oauthService.getAccessToken();
    this.accessTokenSubject.next(this._accessToken);
    this._decodedAccessToken = this.jwtHelper.decodeToken(this._accessToken);

    const token = this._decodedAccessToken;
    const claims = this.getIdentityClaims();

    const fullName = token?.['given_name'] && token?.['family_name']
      ? `${token['given_name']} ${token['family_name']}`
      : '';

    if (fullName) {
      this.usernameSubject.next(fullName);
    }

    if (claims?.['preferred_username']) {
      this.useraliasSubject.next(claims['preferred_username'] as string);
    }
  }
}
