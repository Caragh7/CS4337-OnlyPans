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
import CreatorsPage from "./pages/CreatorsPage";
import YourFeedPage from "./pages/YourFeedPage";
import {fetchCreatorByUserId} from "./api/CreatorServiceApi";



function App() {
    const { keycloak, authenticated, initialized } = useContext(KeycloakContext);
    const [showCreatePost, setShowCreatePost] = useState(false);
    const [isCreator, setIsCreator] = useState(false);

    // creating a post
    const handleToggleCreatePost = () => {
        setShowCreatePost((prev) => !prev);
    };




// checking if a user profile exists for the current logged-in user, and if not we make one!
    const { user, loading, error } = useEnsureUserProfile(
        keycloak?.token,
        authenticated
    );
    const token = keycloak?.token;

// checking if this user is a creator

    useEffect(() => {
        const checkCreatorStatus = async () => {
            if (!user?.id) {
                console.error("User ID is not available");
                return;
            }
            try {
                const creatorProfile = await fetchCreatorByUserId(user.id, token);
                setIsCreator(true); // setting isCreator to true if a profile exists
            } catch (error) {
                if (error.response?.status === 404) {
                    setIsCreator(false); // no creator profile found
                } else {
                    console.error("Error checking creator status:", error);
                }
            }
        };
       checkCreatorStatus();
    }, [authenticated, user, token]);

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
            <ResponsiveAppBar onToggleCreatePost={handleToggleCreatePost} isCreator={isCreator} />
            <Routes>
                <Route
                    path="/profile"
                    element={<UserProfile keycloak={keycloak} authenticated={authenticated} user={user} />}
                />
                <Route
                    path="/upgrade"
                    element={<UpgradeToCreatorProfile keycloak={keycloak} user={user} authenticated={authenticated} isCreator={isCreator} setIsCreator={setIsCreator}/>}
                />


                {/* Main page with buttons */}
                <Route path="/" element={<HomePage />} />

                {/* routes for the other pages */}
                <Route path="/feed" element={<YourFeedPage
                    keycloak={keycloak}
                    authenticated={authenticated}
                    user={user}
                    showCreatePost={showCreatePost}
                    handleToggleCreatePost={handleToggleCreatePost}
                />} />
                <Route path="/creators" element={<CreatorsPage
                    keycloak={keycloak}
                    authenticated={authenticated}
                    user={user} />} />
                <Route path="/posts" element={<AllPostsPage
                    keycloak={keycloak}
                    authenticated={authenticated}
                    user={user}
                    showCreatePost={showCreatePost}
                    handleToggleCreatePost={handleToggleCreatePost}
                />} />
            </Routes>
        </Router>
    );
}


export default App;
