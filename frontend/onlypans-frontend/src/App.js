import { useState, useEffect, useContext } from 'react';
import { KeycloakContext } from './components/KeyCloakContext';
import axios from 'axios';
import './App.css';
import ResponsiveAppBar from './components/appbar';
import CreatePost from './components/createpost';
import PostCard from './components/post';
import useEnsureUserProfile from "./hooks/useEnsureUserProfile";

function App() {
    const { keycloak, authenticated, initialized } = useContext(KeycloakContext);
    const [showCreatePost, setShowCreatePost] = useState(false);
    const [posts, setPosts] = useState([]);

// checking if a user profile exists for the current logged-in user, and if not we make one!
    const { user, loading, error } = useEnsureUserProfile(
        keycloak?.token,
        authenticated
    );

    const handleToggleCreatePost = () => {
        setShowCreatePost((prev) => !prev);
    };

    const handlePostCreate = (newPost) => {
        alert('Post created successfully!');
        setPosts((prevPosts) => [newPost, ...prevPosts]);
    };

    const fetchPosts = async () => {
        if (keycloak && keycloak.authenticated) {
            try {
                const response = await axios.get('http://localhost:8080/posts/', {
                    headers: {
                        Authorization: `Bearer ${keycloak.token}`,
                    },
                });
                setPosts(response.data);
            } catch (error) {
                console.error('Error fetching posts:', error);
            }
        }
    };


    // useEffect(() => {
    //     if (authenticated) {
    //         fetchPosts();
    //     }
    // }, [authenticated]);

    if (!initialized) {
        return <div>Loading...</div>;
    }

    if (!authenticated) {
        keycloak.login();
        return <div>Redirecting to login...</div>;
    }

    // user profile checks!
    if (loading) {
        return <div>Ensuring user profile...</div>
    }

    if (error) {
        return <div>Error ensuring user profile : {error.message}</div>
    }

    return (
        <div>
            {/*<ResponsiveAppBar onToggleCreatePost={handleToggleCreatePost} />*/}
            {/*<CreatePost*/}
            {/*    open={showCreatePost}*/}
            {/*    onClose={handleToggleCreatePost}*/}
            {/*    onPostCreate={handlePostCreate}*/}
            {/*/>*/}

            {/*<div style={styles.scrollableContainer}>*/}
            {/*    {posts.map((post) => (*/}
            {/*        <PostCard key={post.id} post={post} />*/}
            {/*    ))}*/}
            {/*</div>*/}
            <div>
                <h1>Welcome, {user?.firstName || "User"}</h1>
                <p>dis ur email: {user?.email}</p>
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
