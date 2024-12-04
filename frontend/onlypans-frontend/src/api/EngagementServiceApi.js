import axios from "axios";

export const fetchCommentsForPost = async (postId) => {
    try {
        const response = await axios.get(
            `${process.env.REACT_APP_API_GATEWAY_URL}/posts/${postId}/comments`
        );
        return response.data;
    } catch (error) {
        console.error("Error fetching comments:", error);
        throw error;
    }
};

export const addCommentToPost = async (postId, commentText) => {
    try {
        const response = await axios.post(
            `${process.env.REACT_APP_API_GATEWAY_URL}/posts/${postId}/comments`,
            { text: commentText }
        );
        return response.data;
    } catch (error) {
        console.error("Error adding comment:", error);
        throw error;
    }
};
