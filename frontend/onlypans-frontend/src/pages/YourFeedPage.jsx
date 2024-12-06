import React, { useEffect, useState } from 'react';
import { fetchYourFeed } from "../api/PostServiceApi";
import CreatePost from "../components/CreatePost";
import PostCard from "../components/PostCard";
import { CircularProgress, Box, Typography } from '@mui/material';

const YourFeedPage = ({ user, keycloak, authenticated, showCreatePost, handleToggleCreatePost }) => {
    const [posts, setPosts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    console.log(user, authenticated, keycloak)

    const handlePostCreate = (newPost) => {
        setPosts((prevPosts) => [newPost, ...prevPosts]);
    };

    useEffect(() => {
        const fetchFeed = async () => {
            if (authenticated && user) {
                try {
                    const data = await fetchYourFeed(keycloak.token);
                    setPosts(data);
                } catch (err) {
                    setError(err.message || "Failed to fetch feed");
                } finally {
                    setLoading(false);
                }
            } else {
                console.warn("User or authentication state is missing.");
            }
        };

        fetchFeed();
    }, [authenticated, user, keycloak.token]);

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
                keycloak={keycloak}
            />
            <Typography variant="h4" fontWeight="bold" marginBottom="20px" textAlign="center">
                Your Feed
            </Typography>
            {posts.length === 0 ? (
                <Typography variant="h6" textAlign="center">
                    No posts to show. Subscribe to some creators!
                </Typography>
            ) : (
                <Box display="grid" gridTemplateColumns="repeat(auto-fill, minmax(300px, 1fr))" gap="20px">
                    {posts.map((post) => (
                        <PostCard key={post.id} post={post} isSubscribed={true} />
                    ))}
                </Box>
            )}
        </Box>
    );
};

export default YourFeedPage;
