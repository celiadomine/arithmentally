import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivateChildFn, CanActivateFn, Router, RouterStateSnapshot } from '@angular/router';
import { OAuthService } from 'angular-oauth2-oidc';
import { AppAuthService } from '../service/app.auth.service';
import { firstValueFrom } from 'rxjs';

export const appCanActivate: CanActivateFn = async (
  route: ActivatedRouteSnapshot,
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  state: RouterStateSnapshot
): Promise<boolean | ReturnType<Router['parseUrl']>> => {
  const authService = inject(AppAuthService);
  const oauthService = inject(OAuthService);
  const router = inject(Router);

  if (!oauthService.hasValidAccessToken()) {
    return router.parseUrl('/noaccess');
  }

  const userRoles: string[] = await firstValueFrom(authService.getRoles());
  const hasRoles = checkRoles(route, userRoles);

  return hasRoles ? true : router.parseUrl('/noaccess');
};

function checkRoles(route: ActivatedRouteSnapshot, userRoles: string[]): boolean {
  const roles = route.data['roles'] as string[];

  if (!roles || roles.length === 0) {
    return true;
  }

  if (!userRoles) {
    return false;
  }

  return roles.some(role => userRoles.includes(role));
}

export const appCanActivateChild: CanActivateChildFn = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot) =>
  appCanActivate(route, state);
