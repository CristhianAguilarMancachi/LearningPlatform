export const environment = {
  production: false,
  title: 'DemoUCB',
  keycloak: {
    realm: 'DemoUCB',
    clientId: 'lp-web',
    url: 'http://localhost:8082',   // tu Keycloak en docker
  },
  // Base de tu backend (irá por proxy)
  apiUrl: '/ms-res'
};
