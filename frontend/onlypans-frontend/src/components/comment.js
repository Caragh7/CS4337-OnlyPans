import React, { useState, useEffect, useContext } from "react";
import { fetchCommentsForPost, addCommentToPost } from "../api/EngagementServiceApi";
import { Box, Typography, TextField, Button, CircularProgress, List, ListItem, ListItemText, Divider } from "@mui/material";
import { KeycloakContext } from "./KeyCloakContext";

const Comments = ({ postId }) => {
    const { keycloak } = useContext(KeycloakContext);
    const [comments, setComments] = useState([]);
    const [newComment, setNewComment] = useState("");
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        const loadComments = async () => {
            setLoading(true);
            try {
                const token = keycloak?.token;
                if (!token) {
                    console.error("Token not available");
                    return;
                }
                const fetchedComments = await fetchCommentsForPost(postId, token);
                console.log("postid" + postId)
                setComments(fetchedComments);
            } catch (error) {
                console.error("Error fetching comments:", error);
            } finally {
                setLoading(false);
            }
        };

        loadComments();
    }, [postId, keycloak]);

    const handleAddComment = async () => {
        if (!newComment.trim()) return;

        try {
            const token = keycloak?.token;
            if (!token) {
                console.error("Token not available");
                return;
            }

            const addedComment = await addCommentToPost(postId, newComment, token);

            const response = await fetch(`http://localhost:8080/users/${addedComment.userId}`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            const userData = await response.json();

            addedComment.authorName = userData.firstName;
            setComments((prevComments) => [...prevComments, addedComment]);
            setNewComment("");
        } catch (error) {
            console.error("Error adding comment:", error);
        }
    };



    return (
        <div style={{ marginTop: "20px" }}>
            <Divider sx={{ marginBottom: "10px", borderWidth: "1px" }} />
            <Typography variant="h6" gutterBottom>
                Comments
            </Typography>
            {loading ? (
                <div style={{ display: "flex", justifyContent: "center", marginTop: "10px" }}>
                    <CircularProgress />
                </div>
            ) : (
                <List>
                    {comments.length > 0 ? (
                        comments.map((comment) => (
                            <ListItem key={comment.id} divider>
                                <Box
                                    sx={{
                                        display: "flex",
                                        justifyContent: "space-between",
                                        width: "100%",
                                    }}
                                >
                                    <Box>
                                        <Typography variant="body1" fontWeight="bold">
                                            {comment.authorName || "Onlypans user"}
                                        </Typography>
                                        <Typography variant="body2" color="text.secondary">
                                            {comment.text}
                                        </Typography>
                                    </Box>
                                    <Typography
                                        variant="caption"
                                        color="text.secondary"
                                        sx={{
                                            textAlign: "right",
                                            whiteSpace: "nowrap",
                                        }}
                                    >
                                        {new Date(comment.timestamp).toLocaleString("en-GB", {year: "numeric", month: "short", day: "numeric", hour: "2-digit", minute: "2-digit",
                                        })}
                                    </Typography>
                                </Box>
                            </ListItem>
                        ))
                    ) : (
                        <Typography variant="body2" color="text.secondary">
                            No comments yet. Be the first to comment!
                        </Typography>
                    )}
                </List>
            )}
            <div style={{ display: "flex", gap: "10px", marginTop: "10px" }}>
                <TextField
                    label="Enter a comment"
                    variant="outlined"
                    fullWidth
                    value={newComment}
                    onChange={(e) => setNewComment(e.target.value)}
                />
                <Button
                    variant="contained"
                    color="primary"
                    onClick={handleAddComment}
                    disabled={!newComment.trim()}
                >
                    Add
                </Button>
            </div>
        </div>
    );
};

export default Comments;
