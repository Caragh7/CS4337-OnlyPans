import './App.css';
import ResponsiveAppBar from "./components/appbar";
import PostCard from "./components/post";

function App() {
  return (
      <div>
          <ResponsiveAppBar/>

          <div style={styles.scrollableContainer}>
              <PostCard/>
              <PostCard/>
              <PostCard/>
              <PostCard/>
              <PostCard/>
              <PostCard/>
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
