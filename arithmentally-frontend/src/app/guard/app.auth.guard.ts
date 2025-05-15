import { inject } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivateFn,
  Router,
  RouterStateSnapshot,
} from '@angular/router';
import { OAuthService } from 'angular-oauth2-oidc';

export const appCanActivate: CanActivateFn = (
  route: ActivatedRouteSnapshot,
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  state: RouterStateSnapshot
) => {
  const oauthService: OAuthService = inject(OAuthService);
  const router = inject(Router);

  if (!oauthService.hasValidAccessToken()) {
    return router.parseUrl('/noaccess');
  }

  const token = oauthService.getAccessToken();
  const claims: any = oauthService.getIdentityClaims();

  const userRoles: string[] =
    claims?.resource_access?.arithmentally?.roles ?? [];

  const requiredRoles = route.data['roles'] as string[] | undefined;

  // No roles required? Allow access.
  if (!requiredRoles || requiredRoles.length === 0) {
    return true;
  }

  // User must have at least one matching role
  const hasRole = requiredRoles.some((role) =>
    userRoles.includes(`ROLE_${role}`)
  );

  return hasRole ? true : router.parseUrl('/noaccess');
};
