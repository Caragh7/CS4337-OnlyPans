

import React, {useState} from "react";
import { useNavigate } from "react-router-dom";

const HomePage = () => {
    const navigate = useNavigate();

    const buttons = [
        { name: "Your Feed", path: "/page1", backgroundImage: "url('/yourfeed.jpg')" },
        { name: "All Posts", path: "/allPosts", backgroundImage: "url('/allposts.jpg')" },
        { name: "Creators", path: "/page3", backgroundImage: "url('/creators.jpg')" },
    ];

    const [hoverIndex, setHoverIndex] = useState(null);

    return (
        <div
            style={{
                display: "flex",
                flexDirection: "column",
                height: "100vh",
                justifyContent: "space-evenly",
                padding: "20px",
                boxSizing: "border-box",
            }}
        >
            {buttons.map((button, index) => (
                <button
                    key={button.name}
                    onClick={() => navigate(button.path)}
                    onMouseEnter={() => setHoverIndex(index)}
                    onMouseLeave={() => setHoverIndex(null)}
                    style={{
                        flex: "0 1 auto",
                        height: "30%",
                        width: "80%",
                        margin: "0 auto",
                        display: "flex",
                        justifyContent: "center",
                        alignItems: "center",
                        fontSize: "24px",
                        fontWeight: "bold",
                        color: "#fff",
                        textShadow: "1px 1px 2px rgba(0, 0, 0, 0.7)",
                        backgroundImage: button.backgroundImage,
                        backgroundSize: "cover",
                        backgroundPosition: "center",
                        border: "none",
                        outline: "none",
                        cursor: "pointer",
                        borderRadius: "15px",
                        boxShadow: "0 4px 6px rgba(0, 0, 0, 0.1)",
                        transition: "transform 0.3s ease-in-out",
                        transform: hoverIndex === index ? "scale(1.05)" : "scale(1)",

                    }}
                >
                    {button.name}
                </button>
            ))}
        </div>
    );
};

export default HomePage;
