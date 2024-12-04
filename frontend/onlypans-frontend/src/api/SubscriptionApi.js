import axios from "axios";

export const subscribe = async (id, token) => {
    try {
        const response = await axios.post(`${process.env.REACT_APP_API_GATEWAY_URL}/subscriptions/subscribe`, {
            userId: id,
            from: window.location.href.split("/").splice(0,3).join("/")
        },{
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
