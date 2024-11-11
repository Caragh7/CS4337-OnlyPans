import './App.css';
import ResponsiveAppBar from "./components/appbar";
import CreatePost from "./components/createpost";
import PostCard from "./components/post";
import { useState, useEffect } from 'react';
import axios from 'axios';

function App() {
    const [showCreatePost, setShowCreatePost] = useState(false);
    const [posts, setPosts] = useState([]);

    const handleToggleCreatePost = () => {
        setShowCreatePost((prev) => !prev);
    };

    const handlePostCreate = (newPost) => {
        alert("Post created successfully!");
        setPosts((prevPosts) => [newPost, ...prevPosts]);
    };



    // We are returning all posts for the minute, we will need to consider subscriptions, etc.
    useEffect(() => {
        const fetchPosts = async () => {
            try {
                const response = await axios.get('http://localhost:8082/posts');
                setPosts(response.data);
            } catch (error) {
                console.error('Error fetching posts:', error);
            }
        };
        fetchPosts();
    }, []);

    return (
        <div>
            <ResponsiveAppBar onToggleCreatePost={handleToggleCreatePost} />
            <CreatePost open={showCreatePost} onClose={handleToggleCreatePost} onPostCreate={handlePostCreate} />

            <div style={styles.scrollableContainer}>
                {posts.map((post) => (
                    <PostCard key={post.id} post={post} />
                ))}
            </div>
        </div>
    );
}

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

export default App;
