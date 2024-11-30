import { useState, useEffect, useContext } from 'react';
import { KeycloakContext } from './components/KeyCloakContext';
import axios from 'axios';
import './App.css';
import ResponsiveAppBar from './components/appbar';
import CreatePost from './components/createpost';
import PostCard from './components/post';
import useEnsureUserProfile from "./hooks/useEnsureUserProfile";
import UserProfile from "./components/UserService/UserProfile";
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import HomePage from "./pages/HomePage";
import AllPostsPage from "./pages/AllPostsPage";
import UpgradeToCreatorProfile from "./pages/UpgradeToCreatorPage";



function App() {
    const { keycloak, authenticated, initialized } = useContext(KeycloakContext);
    const [showCreatePost, setShowCreatePost] = useState(false);
    const [posts, setPosts] = useState([]);

    // page placeholders
    const Page1 = () => <h1>Page 1</h1>;
    const Page3 = () => <h1>Page 3</h1>;

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


    useEffect(() => {
        if (authenticated) {
            fetchPosts();
        }
    }, [authenticated]);

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
// basic return that works!
//     return (
//
//         <div>
//         <ResponsiveAppBar onToggleCreatePost={handleToggleCreatePost} />
//         <CreatePost
//             open={showCreatePost}
//             onClose={handleToggleCreatePost}
//             onPostCreate={handlePostCreate}
//         />
//
//         <div style={styles.scrollableContainer}>
//             {posts.map((post) => (
//                 <PostCard key={post.id} post={post} />
//             ))}
//         </div>
//         <div>
//             <h1>Welcome, {user?.firstName || "User"}</h1>
//             <p>dis ur email: {user?.email}</p>
//             {/* Pass data as props to UserProfile */}
//             <UserProfile
//                 keycloak={keycloak}
//                 authenticated={authenticated}
//                 user={user}
//             />
//         </div>
//     </div>
//
// );
    return (
        <Router>
            <ResponsiveAppBar onToggleCreatePost={handleToggleCreatePost} />
            <Routes>
                <Route
                    path="/profile"
                    element={<UserProfile keycloak={keycloak} authenticated={authenticated} user={user} />}
                />
                <Route
                    path="/upgrade"
                    element={<UpgradeToCreatorProfile keycloak={keycloak} user={user} authenticated={authenticated} />}
                />
                {/* all posts from db made by marty */}
                <Route
                    path="/allPosts"
                    element={
                        <div>
                            <AllPostsPage
                                posts={posts}
                                showCreatePost={showCreatePost}
                                handlePostCreate={handlePostCreate}
                                handleToggleCreatePost={handleToggleCreatePost}
                            />
                        </div>
                    }
                />
                {/* Main page with buttons */}
                <Route path="/" element={<HomePage />} />

                {/* Placeholder routes for the other pages */}
                <Route path="/page1" element={<Page1 />} />
                <Route path="/allPosts" element={<AllPostsPage />} />
                <Route path="/page3" element={<Page3 />} />
            </Routes>
        </Router>
    );
}


export default App;
