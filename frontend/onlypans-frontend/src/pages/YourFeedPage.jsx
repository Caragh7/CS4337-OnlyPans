import React, { useEffect, useState } from 'react';
import axios from 'axios';
import {fetchYourFeed} from "../api/PostServiceApi";
import CreatePost from "../components/createpost";
import PostCard from "../components/post";

const YourFeedPage = ({ user, keycloak, authenticated, showCreatePost, handleToggleCreatePost }) => {
    const [posts, setPosts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const styles = {
        scrollableContainer: {
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
            justifyContent: 'flex-start',
            padding: '20px',
            height: '100%',
            overflowY: 'scroll',
            scrollbarWidth: 'thin',
            gap: '20px',
        },
    };


    const handlePostCreate = (newPost) => {
        alert('Post created successfully!');
        setPosts((prevPosts) => [newPost, ...prevPosts]);
    };
    useEffect(() => {
        const fetchFeed = async () => {
            if (authenticated && user) {
                try {
                    const data = await fetchYourFeed(keycloak.token);
                    setPosts(data);
                    console.log(posts)
                } catch (err) {
                    console.error("Error fetching your feed:", err);
                    setError(err.message || "Failed to fetch feed");
                } finally {
                    setLoading(false);
                }
            }
        };

        fetchFeed();
    }, [authenticated, user, keycloak]);;


    if (loading) {
        return <div>Loading your feed...</div>;
    }

    if (error) {
        return <div>Error: {error}</div>;
    }

    return (
        <div>
            <CreatePost
                open={showCreatePost}
                onClose={handleToggleCreatePost}
                onPostCreate={handlePostCreate}
                keycloak={keycloak}
            />
            <h1>Your Feed</h1>
            {posts.length === 0 ? (
                <p>No posts to show. Subscribe to some creators!</p>
            ) : (
                <div style={styles.scrollableContainer}>
                    {Array.isArray(posts) ? (
                        posts.map((post) => <PostCard key={post.id} post={post} isSubscribed={true}/>)
                    ) : (
                        <p>No posts available</p>
                    )}
                </div>
            )}
        </div>
    );
};

export default YourFeedPage;
