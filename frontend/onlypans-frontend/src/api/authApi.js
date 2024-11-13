import axios from 'axios';
import keycloak from '../keycloak';

// Auth API functions for login/logout if needed directly
export const login = () => {
    keycloak.login();
};

export const logout = () => {
    keycloak.logout();
};

// Function to get the Keycloak token
export const getAuthToken = () => keycloak.token;

// Axios instance for authenticated requests
const authApi = axios.create();

// Add a request interceptor to include the Authorisation header
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
