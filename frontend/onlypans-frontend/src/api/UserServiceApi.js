import axios from "axios";

export const getUserByUserId = async (id, token) => {
    try {
        const response = await axios.get(`${process.env.REACT_APP_API_GATEWAY_URL}/users/${id}`, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });
        return response.data;
    } catch (error) {
        if (error.response?.status === 404) {
            return null;
        }
        throw error;
    }
};

export const createUser = async (userData,  token) => {
    try {
        const response = await axios.post(`${process.env.REACT_APP_API_GATEWAY_URL}/users/create`, userData, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });
        return response.data;
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
export const upgradeToCreatorProfileReq = async (userId, token, price) => {
    try {
        const response = await axios.post(`${process.env.REACT_APP_API_GATEWAY_URL}/users/upgrade`,{
            price: price.toString()
        },{
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });
        return response.data;
    } catch (error) {
        console.error("Error upgrading user to creator profile:", error);
        throw error;
    }
};

