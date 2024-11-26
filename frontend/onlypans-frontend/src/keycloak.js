import Keycloak from 'keycloak-js';

let keycloakInstance;

export default function getKeycloak() {
    if (!keycloakInstance) {
        keycloakInstance = new Keycloak({
            url: 'http://localhost:8081/',
            realm: 'onlypans',
            clientId: 'onlypans-frontend',
        });
    }
    return keycloakInstance;
}
