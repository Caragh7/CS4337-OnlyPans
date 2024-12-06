import { useEffect, useState } from "react";
import {jwtDecode} from "jwt-decode";
import { createUser, getUserByUserId } from "../api/UserServiceApi";

const extractUserDetailsFromToken = (token) => {
    try {
        const decoded = jwtDecode(token);
        return {
            username: decoded.preferred_username || "",
            firstName: decoded.given_name || "",
            lastName: decoded.family_name || "",
            email: decoded.email || "",
            id: decoded.sub,
        };
    } catch (error) {
        console.error("Error decoding token:", error);
        return null;
    }
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
                if (!userDetails) throw new Error("Invalid token structure");

                let user = await getUserByUserId(userDetails.id, token) ?? await createUser(userDetails, token);
                if (!user) {
                    throw new Error("Unable to create user")
                }
                setUser(user);
            } catch (err) {
                console.error("Error ensuring user profile:", err);
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
