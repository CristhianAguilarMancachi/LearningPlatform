// src/keycloak-init.factory.ts
import { KeycloakService } from 'keycloak-angular';
import { environment } from './environments/environment';

export function initializeKeycloak(keycloak: KeycloakService) {
  return () =>
    keycloak
      .init({
        config: {
          url: environment.keycloak.url,
          realm: environment.keycloak.realm,
          clientId: environment.keycloak.clientId,
        },
        initOptions: {
          // ⚠️ SOLO ESTO, nada de check-sso, ni silentCheckSsoRedirectUri
          onLoad: 'login-required',
          checkLoginIframe: false,
        },
        enableBearerInterceptor: true,
      })
      .then((authenticated) => {
        console.log('[KEYCLOAK] Init OK, authenticated =', authenticated);
      })
      .catch((err) => {
        console.error('[KEYCLOAK] Init ERROR:', err);
      });
}
