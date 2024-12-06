import React, { useEffect, useState } from "react";
import PostCard from "../components/PostCard";
import CreatePost from "../components/CreatePost";
import { fetchPosts, fetchYourFeed } from "../api/PostServiceApi";
import {Box, CircularProgress, Typography} from "@mui/material";

const AllPostsPage = ({ keycloak, authenticated, user, showCreatePost, handleToggleCreatePost }) => {
    const [posts, setPosts] = useState([]);
    const [yourFeed, setYourFeed] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const handlePostCreate = (newPost) => {
        setPosts((prevPosts) => [newPost, ...prevPosts]);
    };

    const token = keycloak?.token;

    useEffect(() => {
        const loadPostsAndFeed = async () => {
            if (token) {
                try {
                    const [fetchedPosts, fetchedFeed] = await Promise.all([
                        fetchPosts(token),
                        fetchYourFeed(token),
                    ]);
                    setPosts(fetchedPosts);
                    setYourFeed(fetchedFeed.map((post) => post.id));
                } catch (error) {
                    setError(error.message || "Failed to fetch posts.");
                } finally {
                    setLoading(false);
                }
            }
        };

        loadPostsAndFeed();
    }, [token]);

    if (loading) {
        return (
            <Box display="flex" justifyContent="center" alignItems="center" height="100vh">
                <CircularProgress />
            </Box>
        );
    }

    if (error) {
        return (
            <Box display="flex" justifyContent="center" alignItems="center" height="100vh">
                <Typography variant="h6" color="error">{error}</Typography>
            </Box>
        );
    }

    return (
        <Box padding="20px">
            <CreatePost
                open={showCreatePost}
                onClose={handleToggleCreatePost}
                onPostCreate={handlePostCreate}
                token={token}
            />
            <Typography variant="h4" fontWeight="bold" marginBottom="20px" textAlign="center">
                All Posts
            </Typography>
            {posts.length === 0 ? (
                <Typography variant="h6" textAlign="center">
                    No posts available. Check back later!
                </Typography>
            ) : (
                <Box
                    display="grid"
                    gridTemplateColumns="repeat(auto-fill, minmax(300px, 1fr))"
                    gap="20px"
                >
                    {posts.map((post) => (
                        <PostCard
                            key={post.id}
                            post={post}
                            isSubscribed={yourFeed.includes(post.id)}
                        />
                    ))}
                </Box>
            )}
        </Box>
    );
};

export default AllPostsPage;
