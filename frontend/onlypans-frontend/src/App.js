import './App.css';
import ResponsiveAppBar from "./components/appbar";
import PostCard from "./components/post";
import CreatePost from "./components/createpost";
import { useState } from 'react';

function App() {
    const [showCreatePost, setShowCreatePost] = useState(false);

    const handleToggleCreatePost = () => {
        setShowCreatePost((prev) => !prev);
    };

    const handlePostCreate = (newPost) => {
        alert("Post created successfully!");
        console.log("New post created:", newPost);
    };

    return (
        <div>
            <ResponsiveAppBar onToggleCreatePost={handleToggleCreatePost} />

            <CreatePost open={showCreatePost} onClose={handleToggleCreatePost} onPostCreate={handlePostCreate} />

            <div style={styles.scrollableContainer}>
                <PostCard />
                <PostCard />
                <PostCard />
                <PostCard />
                <PostCard />
                <PostCard />
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
