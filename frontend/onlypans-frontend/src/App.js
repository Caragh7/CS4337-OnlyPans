import './App.css';
import ResponsiveAppBar from "./components/appbar";
import CreatePost from "./components/createpost";
import PostCard from "./components/post";
import { useState, useEffect } from 'react';
import axios from 'axios';
import keycloak from "./keycloak";

function App() {
    const [authenticated, setAuthenticated] = useState(false);
    const [initialized, setInitialized] = useState(false);
    const [showCreatePost, setShowCreatePost] = useState(false);
    const [posts, setPosts] = useState([]);

    const handleToggleCreatePost = () => {
        setShowCreatePost((prev) => !prev);
    };

    const handlePostCreate = (newPost) => {
        alert("Post created successfully!");
        setPosts((prevPosts) => [newPost, ...prevPosts]);
    };

    const fetchPosts = async () => {
        if (authenticated) {
            try {
                const response = await axios.get('http://localhost:8082/posts', {
                    headers: {
                        Authorization: `Bearer ${keycloak.token}`
                    }
                });
                setPosts(response.data);
            } catch (error) {
                console.error('Error fetching posts:', error);
            }
        }
    };

    useEffect(() => {
        const initKeycloak = async () => {
            if (initialized) return;

            try {
                await keycloak.init({
                    onLoad: 'check-sso',
                    checkLoginIframe: true,
                    pkceMethod: 'S256',
                    flow: 'standard'
                });
                console.log(keycloak)

                if (keycloak.authenticated) {
                    setAuthenticated(true);
                    setInitialized(true);
                } else {
                    // keycloak.login();
                }
            } catch (error) {
                console.error("Keycloak initialization failed:", error);
                setInitialized(true);
            }
        };

        initKeycloak();
    }, [initialized]);

    useEffect(() => {
        if (initialized && authenticated) {
            fetchPosts();
        }
    }, [initialized, authenticated]);

    // Render loading or redirect to login if not authenticated
    if (!initialized) {
        return <div>Loading...</div>;
    }

    if (!authenticated && keycloak && initialized) {
        // keycloak.login();
        return <div>Redirecting to login...</div>;
    }

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
