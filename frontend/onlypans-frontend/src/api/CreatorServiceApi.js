import axios from "axios";

export const deleteCreatorProfile = async (userId, token) => {
    try {
        const response = await axios.delete(
            `${process.env.REACT_APP_API_GATEWAY_URL}/creator-profiles/${userId}`,
            {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            }
        );
        return response.data; // delete success response
    } catch (error) {
        console.error("Error deleting creator profile:", error.response || error.message || error);
        throw error;

    }
}
