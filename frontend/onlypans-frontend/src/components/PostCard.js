import * as React from "react";
import { Card, CardContent, CardMedia, Typography, Avatar, Box, IconButton } from "@mui/material";
import user from "../assets/user.png";
import like from "../assets/icons/like.png";
import comment from "../assets/icons/speech-bubble.png";
import placeholder from "../assets/placeholder.jpg";
import { formatDistanceToNow } from "date-fns";
import { useState, useEffect, useContext } from "react";
import { fetchLikeCount, toggleLike, checkIfUserLiked } from "../api/EngagementServiceApi";
import { KeycloakContext } from "./KeyCloakContext";
import Comments from "./Comment";

function PostCard({ post, isSubscribed }) {
    const { keycloak } = useContext(KeycloakContext);
    const [showComments, setShowComments] = useState(false);
    const [likes, setLikes] = useState(0);
    const [liked, setLiked] = useState(false);
    const token = keycloak?.token;

    const cleanMediaUrl = post.mediaUrl ? post.mediaUrl.split("?")[0] : placeholder;

    const formattedTimestamp = post.timestamp
        ? formatDistanceToNow(new Date(post.timestamp), { addSuffix: true })
        : "";

    useEffect(() => {
        const fetchLikes = async () => {
            try {
                const likeCount = await fetchLikeCount(post.id, token);
                setLikes(likeCount);
                const alreadyLiked = await checkIfUserLiked(post.id, token);
                setLiked(alreadyLiked);
            } catch (error) {}
        };

        fetchLikes();
    }, [post.id, keycloak]);

    const handleLikeToggle = async () => {
        const token = keycloak?.token;
        const userId = keycloak?.tokenParsed?.sub;

        if (!token || !userId) return;

        try {
            const result = await toggleLike(post.id, token);
            if (result === "Like added") {
                setLikes((prev) => prev + 1);
                setLiked(true);
            } else if (result === "Like removed") {
                setLikes((prev) => prev - 1);
                setLiked(false);
            }
        } catch (error) {}
    };

    return (
        <Card
            sx={{
                width: "100%",
                borderRadius: 4,
                boxShadow: 3,
                display: "flex",
                flexDirection: "column",
                marginBottom: 3,
                overflow: "hidden",
                position: "relative",
                backgroundColor: "#fdfdfd",
                transition: "transform 0.3s ease",
                "&:hover": {
                    transform: "scale(1.03)",
                },
            }}
        >
            <CardMedia
                component="img"
                image={cleanMediaUrl}
                alt="post image"
                sx={{
                    width: "100%",
                    height: 300,
                    objectFit: "cover",
                    filter: isSubscribed ? "none" : "blur(8px)",
                    transition: "filter 0.3s ease",
                }}
            />

            {!isSubscribed && (
                <Box
                    sx={{
                        position: "absolute",
                        top: 0,
                        left: 0,
                        right: 0,
                        bottom: 0,
                        display: "flex",
                        alignItems: "center",
                        justifyContent: "center",
                        backgroundColor: "rgba(0, 0, 0, 0.5)",
                        color: "white",
                        fontSize: 16,
                        fontWeight: "bold",
                        textAlign: "center",
                    }}
                >
                    Subscribe to unlock full content!
                </Box>
            )}

            <CardContent sx={{ display: "flex", flexDirection: "column", gap: 2 }}>
                <Box sx={{ display: "flex", alignItems: "center" }}>
                    <Avatar alt={post.authorName || "User"} src={user} />
                    <Box sx={{ ml: 2 }}>
                        <Typography variant="subtitle1" fontWeight="bold">
                            {post.authorName || "OnlyPans User"}
                        </Typography>
                        <Typography variant="caption" color="text.secondary">
                            {formattedTimestamp}
                        </Typography>
                    </Box>
                </Box>

                <Typography variant="body2" color="text.secondary">
                    {isSubscribed
                        ? post.contentDescription || "Check out this recipe!"
                        : "Subscribe to see the full description..."}
                </Typography>

                <Box sx={{ display: "flex", justifyContent: "space-between", mt: 2 }}>
                    <IconButton onClick={handleLikeToggle}>
                        <img
                            src={like}
                            alt="like"
                            style={{
                                height: "24px",
                                width: "24px",
                                opacity: liked ? 1 : 0.5,
                            }}
                        />
                        <Typography variant="body2" sx={{ ml: 1 }}>
                            {likes}
                        </Typography>
                    </IconButton>
                    <IconButton onClick={() => setShowComments((prev) => !prev)}>
                        <img src={comment} alt="comment" style={{ height: "24px", width: "24px" }} />
                    </IconButton>
                </Box>

                {showComments && isSubscribed && <Comments postId={post.id} />}
            </CardContent>
        </Card>
    );
}

export default PostCard;
