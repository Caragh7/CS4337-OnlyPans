

import React, {useState} from "react";
import { useNavigate } from "react-router-dom";

const HomePage = () => {
    const navigate = useNavigate();

    const buttons = [
        { name: "Your Feed", path: "/page1", backgroundImage: "url('/yourfeed.jpg')" },
        { name: "All Posts", path: "/allPosts", backgroundImage: "url('/allposts.jpg')" },
        { name: "Creators", path: "/creators", backgroundImage: "url('/creators.jpg')" },
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
                    position: "relative", // Position relative to contain child layers
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
                    border: "none",
                    outline: "none",
                    cursor: "pointer",
                    borderRadius: "15px",
                    boxShadow: "0 4px 6px rgba(0, 0, 0, 0.1)",
                    overflow: "hidden",
                        transition: "transform 0.3s ease-in-out",
                        transform: hoverIndex === index ? "scale(1.05)" : "scale(1)",
                }}
                >
                    {/* background layer */}
                    <div
                        style={{
                            position: "absolute",
                            top: 0,
                            left: 0,
                            right: 0,
                            bottom: 0,
                            backgroundImage: button.backgroundImage,
                            backgroundSize: "cover",
                            backgroundPosition: "center",
                            filter: "blur(3px)",
                            zIndex: 0,

                        }}
                    ></div>
                    {/* text layer */}
                    <span
                        style={{
                            position: "relative",
                            zIndex: 1,
                        }}
                    >
        {button.name}
    </span>

                </button>
            ))}
        </div>
    );
};

export default HomePage;
