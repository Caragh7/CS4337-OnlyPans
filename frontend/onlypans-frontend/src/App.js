import './App.css';
import ResponsiveAppBar from "./components/appbar";
import CreatePost from "./components/createpost";
import PostCard from "./components/post";
import {useState, useEffect} from 'react';
import axios from 'axios';
import {useKeycloak} from '@react-keycloak/web';

function App() {

    // Pass the token as an Authorisation header with each request

    const {keycloak, initialized} = useKeycloak();
    const [showCreatePost, setShowCreatePost] = useState(false);
    const [posts, setPosts] = useState([]);

    const handleToggleCreatePost = () => {
        setShowCreatePost((prev) => !prev);
    };

    const handlePostCreate = (newPost) => {
        alert("Post created successfully!");
        setPosts((prevPosts) => [newPost, ...prevPosts]);
    };

    // Fetch posts only if authenticated
    useEffect(() => {
        const fetchPosts = async () => {
          
            if (keycloak.authenticated) {
                // test line to check if token was being printed in the console
                // console.log("Keycloak Token:", keycloak.token);
                try {
                    const response = await axios.get('http://localhost:8080/posts', {
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

        if (initialized) {
            fetchPosts();
        }
    }, [initialized, keycloak.authenticated, keycloak.token]);

    // Render loading or redirect to login if not authenticated
    if (!initialized) {
        return <div>Loading...</div>;
    }

    if (!keycloak.authenticated) {
        keycloak.login();
        return <div>Redirecting to login...</div>;
    }

    return (
        <div>
            <ResponsiveAppBar onToggleCreatePost={handleToggleCreatePost}/>
            <CreatePost open={showCreatePost} onClose={handleToggleCreatePost} onPostCreate={handlePostCreate}/>

            <div style={styles.scrollableContainer}>
                {posts.map((post) => (
                    <PostCard key={post.id} post={post}/>
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
