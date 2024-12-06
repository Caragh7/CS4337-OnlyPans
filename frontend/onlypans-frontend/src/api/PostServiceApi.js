import axios from 'axios';

export const getPresignedUrl = async (fileName, token) => {
    try {
        const { data } = await axios.get(`${process.env.REACT_APP_API_GATEWAY_URL}/media/presigned-url`, {
            params: { fileName },
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });
        return data;
    } catch (error) {
        console.error('Error fetching presigned URL:', error);
        throw error;
    }
};

export const uploadFile = async (presignedUrl, file) => {
    try {
        await axios.put(presignedUrl, file, {
            headers: { 'Content-Type': file.type },
        });
    } catch (error) {
        console.error('Error uploading file:', error);
        throw error;
    }
};

export const createPost = async (postContent, token) => {
    try {
        const { data } = await axios.post(`${process.env.REACT_APP_API_GATEWAY_URL}/posts`, postContent, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });
        return data;
    } catch (error) {
        console.error('Error creating post:', error);
        throw error;
    }
};


export const fetchPosts = async (token) => {
    try {
        const { data } = await axios.get(`${process.env.REACT_APP_API_GATEWAY_URL}/posts`, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });
        return data || [];
    } catch (error) {
        console.error('Error fetching posts:', error);
        throw error;
    }
};

export const fetchYourFeed = async (token) => {
    try {
        const {data } = await axios.get(`${process.env.REACT_APP_API_GATEWAY_URL}/posts/your-feed`, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });
        return data || [];
    } catch (error) {
        console.error("Error fetching your feed:", error);
        throw error;
    }
};
