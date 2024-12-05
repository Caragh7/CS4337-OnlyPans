

import React, {useEffect, useState} from "react";
import PostCard from "../components/post";
import CreatePost from "../components/createpost";
import {fetchPosts} from "../api/PostServiceApi"

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


const AllPostsPage = ({keycloak, authenticated, user, showCreatePost, handleToggleCreatePost}) => {
    const [posts, setPosts] = useState([]);

    const handlePostCreate = (newPost) => {
        alert('Post created successfully!');
        setPosts((prevPosts) => [newPost, ...prevPosts]);
    };

    const token = keycloak?.token;

    useEffect(() => {
        const loadPosts = async () => {
            if (token) {
                try {
                    const fetchedPosts = await fetchPosts(token);
                    setPosts(fetchedPosts);
                } catch (error) {
                    console.error("Error fetching posts:", error);
                    setPosts([]); // Fallback to an empty array
                }
            }
        };

        loadPosts();
    }, [token]);


    return (
        <div>
            <div>
                <CreatePost
                    open={showCreatePost}
                    onClose={handleToggleCreatePost}
                    onPostCreate={handlePostCreate}
                    token={token}
                />
            </div>
            <div style={styles.scrollableContainer}>
                {Array.isArray(posts) ? (
                    posts.map((post) => <PostCard key={post.id} post={post} />)
                ) : (
                    <p>No posts available</p>
                )}
            </div>
        </div>
    );
};


export default AllPostsPage;
