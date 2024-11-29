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
        throw error; // Propagate other errors
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
