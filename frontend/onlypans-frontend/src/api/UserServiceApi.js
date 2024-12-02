import axios from "axios";



// function to check if a user exists by email (email comes from token!)
export const getUserByEmail = async (email, token) => {
    try {
        const response = await axios.get(`${process.env.REACT_APP_API_GATEWAY_URL}/users/by-email`, {
            params: { email },
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });
        return response.data; // returning user data if found (they already exists in db)
    } catch (error) {
        if (error.response?.status === 404) {
            return null; // if user not found, means we need to make one!
        }
        throw error; // propagate other errors from api call
    }
};

// function to create a new user
export const createUser = async (userData,  token) => {
    try {
        const response = await axios.post(`${process.env.REACT_APP_API_GATEWAY_URL}/users/create`, userData, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });
        return response.data; // returning new created user
    } catch (error) {
        console.error("Error creating user:", error.response || error.message || error);
        throw error;
    }
};
export const updateUser = async (userId, userData, token) => {
    try {
        const response = await axios.put(
            `${process.env.REACT_APP_API_GATEWAY_URL}/users/${userId}`,
            userData,
            {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error updating user:", error.response || error.message || error);
        throw error;
    }
};

export const deleteUser = async (userId, token) => {
    try {
        await axios.delete(`${process.env.REACT_APP_API_GATEWAY_URL}/users/${userId}`, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });
        return true;
    } catch (error) {
        console.error("Error deleting user:", error.response || error.message || error);
        throw error;
    }
};
// Function to upgrade a user to a creator profile
export const upgradeToCreatorProfileReq = async (userId, token) => {
    try {
        const response = await axios.post(`${process.env.REACT_APP_API_GATEWAY_URL}/users/${userId}/upgrade`,{},{
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });
        return response.data; // Return success message or other data
    } catch (error) {
        console.error("Error upgrading user to creator profile:", error);
        throw error; // Re-throw error for the calling component to handle
    }
};

