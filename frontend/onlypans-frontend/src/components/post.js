import * as React from 'react';
import { Card, CardContent, CardMedia, Typography, Avatar, Box, IconButton } from '@mui/material';
import user from '../assets/user.png'
import like from '../assets/icons/like.png'
import comment from '../assets/icons/speech-bubble.png'
import placeholder from '../assets/placeholder.jpg'

function PostCard() {
    return (
        <Card sx={{ maxWidth: 750, borderRadius: 4, boxShadow: 3 }}>
            <CardMedia
                component="img"
                height="400"
                image={placeholder}
                alt="food picture"
            />

            <CardContent>
                {/* Page button placeholders */}
                <Box sx={{ display: 'flex', justifyContent: 'center', mt: 2 }}>
                    <Box sx={{ width: 10, height: 10, bgcolor: 'black', borderRadius: '50%', mx: 0.5 }} />
                    <Box sx={{ width: 10, height: 10, bgcolor: 'gray', borderRadius: '50%', mx: 0.5 }} />
                    <Box sx={{ width: 10, height: 10, bgcolor: 'gray', borderRadius: '50%', mx: 0.5 }} />
                </Box>

                <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                    <Avatar alt="John Doe" src={user} />
                    <Box sx={{ ml: 2 }}>
                        <Typography variant="body2" color="text.primary">
                            OnlyPans User
                        </Typography>
                    </Box>
                </Box>


                {/* post content */}
                <Typography variant="body2" color="text.secondary">
                    pay for my OnlyPans and see more! 🤤
                </Typography>

                <Box sx={{ display: 'flex', justifyContent: 'flex-end', mt: 2 }}>
                    <IconButton aria-label="like">
                        <img src={like} alt="like" style={{ height: '24px', width: '24px' }} />
                    </IconButton>
                    <IconButton aria-label="comment">
                        <img src={comment} alt="comment" style={{ height: '24px', width: '24px' }} />
                    </IconButton>
                </Box>
            </CardContent>
        </Card>
    );
}

export default PostCard;
