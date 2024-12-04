import * as React from 'react';
import { Card, CardContent, CardMedia, Typography, Avatar, Box, IconButton, Divider } from '@mui/material';
import user from '../assets/user.png';
import like from '../assets/icons/like.png';
import comment from '../assets/icons/speech-bubble.png';
import placeholder from '../assets/placeholder.jpg';
import { formatDistanceToNow } from 'date-fns';
import {useState} from "react";
import Comments from './comment'

function PostCard({ post }) {

    const [showComments, setShowComments] = useState(false);
    const cleanMediaUrl = post.mediaUrl ? post.mediaUrl.split('?')[0] : placeholder;

    const formattedTimestamp = post.timestamp
        ? formatDistanceToNow(new Date(post.timestamp), { addSuffix: true })
        : '';

    const handleCommentClick = () => {
        setShowComments((prev) => !prev);
    };

    console.log(cleanMediaUrl);

    return (
        <Card
            sx={{
                width: 600,
                borderRadius: 4,
                boxShadow: 3,
                display: 'flex',
                flexDirection: 'column',
                marginBottom: 3,
            }}
        >
            <CardMedia
                component="img"
                image={cleanMediaUrl}
                alt="post image"
                sx={{
                    width: '100%',
                    height: 600,
                    objectFit: 'cover',
                }}
            />

            <CardContent
                sx={{
                    flex: 1,
                    display: 'flex',
                    flexDirection: 'column',
                    justifyContent: 'space-between',
                }}
            >
                <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                    <Avatar alt={post.authorName || 'User'} src={user} />
                    <Box sx={{ ml: 2 }}>
                        <Typography variant="body2" color="text.primary">
                            {post.authorName || 'OnlyPans User'}{' '}
                            {formattedTimestamp && `- ${formattedTimestamp}`}
                        </Typography>
                    </Box>
                </Box>

                <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                    {post.contentDescription || 'Check out this recipe!'}
                </Typography>

                <Box sx={{ display: 'flex', justifyContent: 'flex-end', gap: 2 }}>
                    <IconButton aria-label="like">
                        <img src={like} alt="like" style={{ height: '24px', width: '24px' }} />
                    </IconButton>
                    <IconButton aria-label="comment" onClick={handleCommentClick}>
                        <img src={comment} alt="comment" style={{ height: '24px', width: '24px' }} />
                    </IconButton>
                </Box>

                {showComments && <Comments postId={post.id} />}
            </CardContent>
        </Card>
    );
}

export default PostCard;