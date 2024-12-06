import {useState, useEffect, useContext, useRef} from 'react';
import {KeycloakContext} from './components/KeyCloakContext';
import axios from 'axios';
import './App.css';
import ResponsiveAppBar from './components/appbar';
import CreatePost from './components/createpost';
import PostCard from './components/post';
import useEnsureUserProfile from "./hooks/useEnsureUserProfile";
import UserProfile from "./components/UserService/UserProfile";
import {BrowserRouter as Router, Routes, Route} from 'react-router-dom';
import HomePage from "./pages/HomePage";
import AllPostsPage from "./pages/AllPostsPage";
import UpgradeToCreatorProfile from "./pages/UpgradeToCreatorPage";
import CreatorsPage from "./pages/CreatorsPage";
import {sendLoginNotification} from './api/EmailNotificationApi';


function App() {
    const {keycloak, authenticated, initialized} = useContext(KeycloakContext);
    const [showCreatePost, setShowCreatePost] = useState(false);

    // creating a post
    const handleToggleCreatePost = () => {
        setShowCreatePost((prev) => !prev);
    };


    // page placeholders
    const Page1 = () => <h1>Page 1</h1>;
    const Page3 = () => <h1>Page 3</h1>;

// checking if a user profile exists for the current logged-in user, and if not we make one!
    const {user, loading, error} = useEnsureUserProfile(
        keycloak?.token,
        authenticated
    );


    // Track if email has been sent
    const setNotificationSent = () => {
        sessionStorage.setItem("notificationSent", "true");
    };

// Check if email has been sent
    const isNotificationSent = () => {
        return sessionStorage.getItem("notificationSent") === "true";
    };

// Call sendLoginNotification if auth succeeds
    useEffect(() => {
        if (user && authenticated && !isNotificationSent()) {
            sendLoginNotification(user.email, keycloak.token)
                .then(() => {
                    console.log("Login notification sent successfully!");
                    setNotificationSent();
                })
                .catch((error) => {
                    console.error("Failed to send login notification:", error);
                });
        }
    }, [user, authenticated, keycloak?.token]);


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
        <Router>
            <ResponsiveAppBar onToggleCreatePost={handleToggleCreatePost}/>
            <Routes>
                <Route
                    path="/profile"
                    element={<UserProfile keycloak={keycloak} authenticated={authenticated} user={user}/>}
                />
                <Route
                    path="/upgrade"
                    element={<UpgradeToCreatorProfile keycloak={keycloak} user={user} authenticated={authenticated}/>}
                />


                {/* Main page with buttons */}
                <Route path="/" element={<HomePage/>}/>

                {/* Placeholder routes for the other pages */}
                <Route path="/page3" element={<Page3/>}/>
                <Route path="/creators"
                       element={<CreatorsPage keycloak={keycloak} authenticated={authenticated} user={user}/>}/>
                <Route path="/allPosts" element={<AllPostsPage
                    keycloak={keycloak}
                    authenticated={authenticated}
                    user={user}
                    showCreatePost={showCreatePost}
                    handleToggleCreatePost={handleToggleCreatePost}
                />}/>
            </Routes>
        </Router>
    );
}


export default App;
