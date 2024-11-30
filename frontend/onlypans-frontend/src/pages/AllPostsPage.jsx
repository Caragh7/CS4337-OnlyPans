

import React from "react";
import PostCard from "../components/post";
import CreatePost from "../components/createpost";

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
const AllPostsPage = ({ posts, showCreatePost, handleToggleCreatePost, handlePostCreate }) => {
    return (
        <div>
            <div>
                <CreatePost
                    open={showCreatePost}
                    onClose={handleToggleCreatePost}
                    onPostCreate={handlePostCreate}
                />
            </div>
            <div style={styles.scrollableContainer}>
                {posts.map((post) => (
                    <PostCard key={post.id} post={post} />
                ))}
            </div>
        </div>
    );
};


export default AllPostsPage;
