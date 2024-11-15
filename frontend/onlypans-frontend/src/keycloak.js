// src/keycloak.js
import Keycloak from 'keycloak-js';

const keycloak = new Keycloak({
    // Please enter the credentials provided on discord here
    url: 'http://localhost:8081/',
    realm: 'onlypans',
    clientId: 'onlypans-frontend'
});

export default keycloak;
