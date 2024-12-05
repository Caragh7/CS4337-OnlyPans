import React, { useEffect, useState } from "react";
import { fetchAllCreators } from "../api/CreatorServiceApi";
import { subscribe } from "../api/SubscriptionApi";
import { loadStripe } from "@stripe/stripe-js";

const CreatorsPage = ({ keycloak, authenticated, user }) => {
    const [creators, setCreators] = useState([]);
    const [loading, setLoading] = useState(true);
    const [stripe, setStripe] = useState(null);

    const token = keycloak?.token;

    useEffect(() => {
        const initializeStripe = async () => {
            const stripeObj = await loadStripe(process.env.REACT_APP_STRIPE_KEY);
            setStripe(stripeObj);
        };
        initializeStripe();
    }, []);

    const handleSubscription = async (id) => {
        try {
            const response = await subscribe(id, token);
            console.log(response)
            const sessionId = response;
            if (stripe && sessionId) {
                await stripe.redirectToCheckout({ sessionId });
            } else {
                console.error("Stripe not initialized or sessionId missing");
            }
        } catch (error) {
            console.error("Subscription failed:", error);
        }
    };

    useEffect(() => {
        const getCreators = async () => {
            try {
                const data = await fetchAllCreators(token);
                setCreators(data);
            } catch (error) {
                console.error("Failed to load creators:", error);
            } finally {
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
            <div
                style={{
                    display: "grid",
                    gridTemplateColumns: "repeat(auto-fill, minmax(300px, 1fr))",
                    gap: "20px",
                    padding: "20px",
                }}
            >
                {creators
                    .filter((creator) => creator.userId !== user.id)
                    .map((creator) => (
                    <CreatorCard
                        token={token}
                        onSubscribe={handleSubscription}
                        key={creator.id}
                        creator={creator}
                    />
                ))}
            </div>
        </div>
    );
};

const CreatorCard = ({ token, creator, onSubscribe }) => {
    const placeholderImage = "mark_zuc.jpg";

    return (
        <div
            style={{
                borderRadius: "15px",
                overflow: "hidden",
                boxShadow: "0px 4px 8px rgba(0, 0, 0, 0.1)",
                textAlign: "center",
                width: "100%",
            }}
        >
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
                    onClick={() => {
                        onSubscribe(creator.userId);
                    }}
                >
                    Subscribe
                </button>
            </div>
        </div>
    );
};

export default CreatorsPage;
