import { useEffect, useState } from "react";
import {jwtDecode} from "jwt-decode";
import { getUserById, createUser } from "../api/UserServiceApi";

const extractUserDetailsFromToken = (token) => {
    const decoded = jwtDecode(token);
    console.log(decoded)
    return {
        username : decoded.preferred_username || "",
        firstName: decoded.given_name || "",
        lastName: decoded.family_name || "",
        email: decoded.email || "",
        id: decoded.sub,
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
                const userDetails = extractUserDetailsFromToken(token);
                console.log(userDetails)

                let user = await getUserById(userDetails.id, token);

                if (!user) {
                    user = await createUser(userDetails, token);
                }

                setUser(user);
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
