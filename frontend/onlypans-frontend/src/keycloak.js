import Keycloak from 'keycloak-js';

let keycloakInstance;

export default function getKeycloak() {
    if (!keycloakInstance) {
        console.log('Initializing Keycloak with URL:', process.env.KEYCLOAK_URL);
        keycloakInstance = new Keycloak({
            url: process.env.REACT_APP_KEYCLOAK_URL,
            realm: 'onlypans',
            clientId: 'onlypans-frontend',
        });
    }
    return keycloakInstance;
}

