import { useEffect, useState } from "react";
import {jwtDecode} from "jwt-decode";
import { getUserByEmail, createUser } from "../api/UserServiceApi";

const extractUserDetailsFromToken = (token) => {
    const decoded = jwtDecode(token);
    return {
        username : decoded.preferred_username || "",
        firstName: decoded.given_name || "",
        lastName: decoded.family_name || "",
        email: decoded.email || "",
        keycloakId: decoded.sub, // unique Keycloak user id, idk if this should replace userID in the db :/
    };
};

const useEnsureUserProfile = (token, authenticated) => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        const ensureUserProfile = async () => {
            if (!token || !authenticated) return;

            setLoading(true);
            try {
                // extract user details from the token using decode function above
                const userDetails = extractUserDetailsFromToken(token);

                // checking if user exists in db
                let user = await getUserByEmail(userDetails.email, token);

                // if user doesn't exist, then we create one
                if (!user) {
                    user = await createUser(userDetails, token);
                }

                setUser(user); // save user data to state
            } catch (err) {
                setError(err);
            } finally {
                setLoading(false);
            }
        };

        ensureUserProfile();
    }, [token, authenticated]);

    return { user, loading, error };
};

export default useEnsureUserProfile;
