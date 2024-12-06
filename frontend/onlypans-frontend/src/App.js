import {useState, useEffect, useContext} from "react";
import {KeycloakContext} from "./components/KeyCloakContext";
import {BrowserRouter as Router, Routes, Route} from "react-router-dom";
import ResponsiveAppBar from "./components/AppBar";
import CreatePost from "./components/CreatePost";
import useEnsureUserProfile from "./hooks/useEnsureUserProfile";
import UserProfile from "./components/UserService/UserProfile";
import HomePage from "./pages/HomePage";
import AllPostsPage from "./pages/AllPostsPage";
import UpgradeToCreatorProfile from "./pages/UpgradeToCreatorPage";
import CreatorsPage from "./pages/CreatorsPage";
import YourFeedPage from "./pages/YourFeedPage";
import {fetchCreatorByUserId} from "./api/CreatorServiceApi";
import {CircularProgress, Box} from "@mui/material";
import {sendLoginNotification} from "./api/EmailNotificationApi";

function App() {
    const {keycloak, authenticated, initialized} = useContext(KeycloakContext);
    const [showCreatePost, setShowCreatePost] = useState(false);
    const [isCreator, setIsCreator] = useState(false);

    const {user, loading: profileLoading, error: profileError} = useEnsureUserProfile(
        keycloak?.token,
        authenticated
    );

    const token = keycloak?.token;

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
    const handleToggleCreatePost = () => setShowCreatePost((prev) => !prev);

    useEffect(() => {
        if (user?.id) {
            const checkCreatorStatus = async () => {
                try {
                    const data = await fetchCreatorByUserId(user.id, token);
                    if (data) {
                        setIsCreator(true)
                    } else {
                        setIsCreator(false)
                    }
                } catch (error) {
                    if (error.response?.status === 404) setIsCreator(false);
                    else console.error("Error checking creator status:", error);
                }
            };
            checkCreatorStatus();
        }
    }, [user?.id, token]);

    const renderLoader = (message = "Loading...") => (
        <Box
            sx={{
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                justifyContent: "center",
                height: "100vh",
            }}
        >
            <CircularProgress/>
            <Box sx={{mt: 2, fontSize: 16, color: "#666"}}>{message}</Box>
        </Box>
    );

    if (!initialized) return renderLoader("Initializing application...");
    if (!authenticated) {
        keycloak.login();
        return renderLoader("Redirecting to login...");
    }
    if (profileLoading) return renderLoader("Setting up your profile...");
    if (profileError)
        return (
            <Box
                sx={{
                    display: "flex",
                    flexDirection: "column",
                    alignItems: "center",
                    justifyContent: "center",
                    height: "100vh",
                    color: "red",
                    fontSize: 16,
                }}
            >
                Error ensuring user profile: {profileError.message}
            </Box>
        );

    return (
        <Router>
            <ResponsiveAppBar onToggleCreatePost={handleToggleCreatePost} isCreator={isCreator}/>
            <Routes>
                <Route path="/profile" element={<UserProfile keycloak={keycloak} user={user}/>}/>
                <Route
                    path="/upgrade"
                    element={
                        <UpgradeToCreatorProfile
                            keycloak={keycloak}
                            user={user}
                            isCreator={isCreator}
                            setIsCreator={setIsCreator}
                            authenticated={authenticated}
                        />
                    }
                />
                <Route path="/" element={<HomePage/>}/>
                <Route
                    path="/feed"
                    element={
                        <YourFeedPage
                            keycloak={keycloak}
                            user={user}
                            authenticated={keycloak.authenticated}
                            showCreatePost={showCreatePost}
                            handleToggleCreatePost={handleToggleCreatePost}
                        />
                    }
                />
                <Route
                    path="/creators"
                    element={<CreatorsPage keycloak={keycloak} user={user}/>}
                />
                <Route
                    path="/posts"
                    element={
                        <AllPostsPage
                            keycloak={keycloak}
                            user={user}
                            authenticated={authenticated}
                            showCreatePost={showCreatePost}
                            handleToggleCreatePost={handleToggleCreatePost}
                        />
                    }
                />
            </Routes>
        </Router>
    );
}

export default App;