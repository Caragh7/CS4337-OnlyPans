import { useEffect, useState } from "react";
import {jwtDecode} from "jwt-decode";
import { getUserById, createUser, getUserByUserId } from "../api/UserServiceApi";

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

                let user = null;

                try {
                    user = await getUserByUserId(userDetails.id, token);
                } catch (err) {
                    if (err.response?.status === 404) {
                        console.log("User not found, creating new user...");
                        user = await createUser(userDetails, token);
                    } else {
                        throw err;
                    }
                }

                if (!user) throw new Error("User creation failed");

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
