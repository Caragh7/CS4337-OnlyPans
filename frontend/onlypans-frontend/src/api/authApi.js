import axios from 'axios';
import keycloak from '../keycloak';

export const login = () => {
    keycloak.login();
};

export const logout = () => {
    keycloak.logout();
};

export const getAuthToken = () => keycloak.token;

const authApi = axios.create();

authApi.interceptors.request.use(
    (config) => {
        const token = getAuthToken();
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

export default authApi;
