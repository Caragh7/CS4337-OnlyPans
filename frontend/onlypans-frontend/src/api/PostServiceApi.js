import axios from "axios";


export const fetchPosts = async (token) => {
        try {
            const response = await axios.get(`${process.env.REACT_APP_API_GATEWAY_URL}/posts`,{
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });

            console.log("API Response:", response.data);
             return response.data || [];
        } catch (error) {
            console.error('Error fetching posts:', error);
        }

};

export const fetchYourFeed = async (token) => {
    try {
        const response = await axios.get(`${process.env.REACT_APP_API_GATEWAY_URL}/posts/your-feed`, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });
        console.log("Your Feed API Response:", response.data);
        return response.data || [];
    } catch (error) {
        console.error("Error fetching your feed:", error);
        return [];
    }
};