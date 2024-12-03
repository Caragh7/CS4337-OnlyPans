import axios from "axios";

export const deleteCreatorProfile = async (userId, token) => {
    try {
        const response = await axios.delete(
            `${process.env.REACT_APP_API_GATEWAY_URL}/creator-profiles/delete`, 
            {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error deleting creator profile:", error.response || error.message || error);
        throw error;

    }
}

export const fetchAllCreators = async (token) => {
    try {
        const response = await axios.get(
            `${process.env.REACT_APP_API_GATEWAY_URL}/creator-profiles`,
            {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error fetching creators:", error.response || error.message);
        throw error;
    }
};

export const fetchCreatorByUserId = async (userId, token) => {
    try {
        const response = await axios.get(
            `${process.env.REACT_APP_API_GATEWAY_URL}/creator-profiles/user/${userId}`,
            {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error fetching creator profile by userId:", error.response || error.message);
        throw error;
    }
};