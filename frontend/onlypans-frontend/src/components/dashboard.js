import React from "react";
import PostCard from "./post";

function Dashboard() {
    return (<div style={styles.scrollableContainer}>
        <PostCard/>
        <PostCard/>
        <PostCard/>
    </div>)

}
export default Dashboard;

const styles = {
    Dashboard:{

    },
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