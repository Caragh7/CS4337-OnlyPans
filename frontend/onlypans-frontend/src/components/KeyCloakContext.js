// src/KeycloakContext.js
import React, { createContext, useState, useEffect } from 'react';
import Keycloak from 'keycloak-js';

export const KeycloakContext = createContext();

export const KeycloakProvider = ({ children }) => {
    const [keycloak, setKeycloak] = useState(null);
    const [initialized, setInitialized] = useState(false);
    const [authenticated, setAuthenticated] = useState(false);

    useEffect(() => {
        const keycloakInstance = new Keycloak({
            url: 'http://localhost:8081/',
            realm: 'onlypans',
            clientId: 'onlypans-frontend',
        });

        keycloakInstance
            .init({
                onLoad: 'check-sso',
                checkLoginIframe: true,
                pkceMethod: 'S256',
                flow: 'standard',
            })
            .then((auth) => {
                setKeycloak(keycloakInstance);
                setAuthenticated(auth);
                setInitialized(true);
            })
            .catch((error) => {
                console.error('Keycloak initialization failed:', error);
                setInitialized(true);
            });
    }, []);

    return (
        <KeycloakContext.Provider value={{ keycloak, initialized, authenticated }}>
            {children}
        </KeycloakContext.Provider>
    );
};
