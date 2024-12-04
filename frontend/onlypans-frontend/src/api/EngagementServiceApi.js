import axios from "axios";

const API_BASE_URL = process.env.REACT_APP_API_GATEWAY_URL || "http://localhost:8080";

export const fetchCommentsForPost = async (postId, token) => {
    try {
        const response = await axios.get(`${API_BASE_URL}/engagements/comments/${postId}`, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });
        return response.data;
    } catch (error) {
        console.error("Error fetching comments:", error.response || error.message);
        throw error;
    }
};

export const addCommentToPost = async (postId, commentBody, token) => {
    try {
        const response = await axios.post(
            `${API_BASE_URL}/engagements/comments`,
            { postId, commentBody }, // Correct payload for the backend
            {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error adding comment:", error.response || error.message);
        throw error;
    }
};