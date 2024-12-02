import React, { useEffect, useState } from "react";
import { fetchAllCreators } from "../api/CreatorServiceApi"; // Adjust the path as per your project structure


const CreatorsPage = ({keycloak, authenticated, user}) => {
    const [creators, setCreators] = useState([]);
    const [loading, setLoading] = useState(true);


    const token = keycloak?.token;

    useEffect(() => {
        const getCreators = async () => {
            try {
                const data = await fetchAllCreators(token);
                setCreators(data);
                setLoading(false);
            } catch (error) {
                console.error("Failed to load creators:", error);
                setLoading(false);
            }
        };

        if (token) {
            getCreators();
        }
    }, [token]);

    if (loading) return <p>Loading...</p>;

    return (
        <div>
            <h1 style={{ textAlign: "center", margin: "20px 0" }}>All Creators</h1>
            <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fill, minmax(300px, 1fr))", gap: "20px", padding: "20px" }}>
                {creators.map((creator) => (
                    <CreatorCard key={creator.id} creator={creator} />
                ))}
            </div>
        </div>
    );
};
const CreatorCard = ({ creator }) => {
    const placeholderImage = "mark_zuc.jpg"; // mark zuc placeholder
    return (
        <div
            style={{
                borderRadius: "15px",
                overflow: "hidden", // Ensures the split design works
                boxShadow: "0px 4px 8px rgba(0, 0, 0, 0.1)",
                textAlign: "center",
                width: "100%",
            }}
        >
            {/* Top section: Blue background */}
            <div
                style={{
                    backgroundColor: "#007BFF",
                    padding: "20px",
                    color: "white",
                }}
            >
                <img
                    src={creator.profilePicture || placeholderImage}
                    alt={`${creator.firstName} ${creator.lastName}`}
                    style={{
                        borderRadius: "50%",
                        width: "100px",
                        height: "100px",
                        marginBottom: "10px",
                        border: "3px solid white",
                    }}
                />
                <h2>{`${creator.firstName} ${creator.lastName}`}</h2>
            </div>

            {/* Bottom section: White background */}
            <div
                style={{
                    backgroundColor: "white",
                    padding: "20px",
                    color: "#333",
                }}
            >
                <p>{creator.bio || "This creator has no bio yet."}</p>
                <button
                    style={{
                        backgroundColor: "#007BFF",
                        color: "white",
                        border: "none",
                        padding: "10px 20px",
                        borderRadius: "20px",
                        cursor: "pointer",
                        fontWeight: "bold",
                    }}
                    onClick={() => alert(`Subscribed to ${creator.firstName} ${creator.lastName}`)}
                >
                    Subscribe
                </button>
            </div>
        </div>
    );
};

export default CreatorsPage;
