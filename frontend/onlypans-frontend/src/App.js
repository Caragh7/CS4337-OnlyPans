import './App.css';
import ResponsiveAppBar from "./components/appbar";
import {Route, Routes} from 'react-router-dom';
import Login from "./components/login";
import Dashboard from "./components/dashboard";
import {useEffect, useState} from "react";
import axios from "axios";

function App() {
    // use axios to access the auth token
    const [authToken, setAuthToken] = useState(null);

    useEffect(() => {
        axios.get("http://localhost:8080/oauth-token", {withCredentials: true})
            .then(response => {
                setAuthToken(response.data); // Store the token
                console.log(response.data);
            })

            .catch(error => {
                // Print error to console if it doesn't find the token
                console.error("Error fetching auth token:", error);
            });
    }, []);

    return (
        <div>
            <ResponsiveAppBar/>

            {/* Define Routes for navigation */}
            <Routes>
                {/* Login page route */}
                <Route path="/" element={<Login/>}/>
                {/* Dashboard route */}
                <Route path="/dashboard" element={<Dashboard/>}/>
            </Routes>
        </div>
    );
}


export default App;
