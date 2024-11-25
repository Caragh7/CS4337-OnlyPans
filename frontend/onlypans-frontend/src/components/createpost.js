import axios from 'axios';
import * as React from 'react';
import { useState } from 'react';
import { Card, CardContent, CardMedia, Typography, Avatar, Box, TextField, Button, Modal, Backdrop, Snackbar, Alert } from '@mui/material';
import placeholder from '../assets/placeholder.jpg';
import user from '../assets/user.png';

function CreatePost({ open, onClose }) {
    const [content, setContent] = useState('');
    const [image, setImage] = useState(null);
    const [preview, setPreview] = useState(placeholder);
    const [successOpen, setSuccessOpen] = useState(false);

    const handleImageChange = (e) => {
        const file = e.target.files[0];
        if (file) {
            setImage(file);
            setPreview(URL.createObjectURL(file));
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!image) {
            alert('Please upload an image!');
            return;
        }


        console.log("Requesting presigned URL")
        try {
            const fileName = image.name;
            const { data: presignedUrl } = await axios.get('http://localhost:8080/media/presigned-url', {
                params: { fileName }
            });

            await axios.put(presignedUrl, image, {
                headers: {
                    'Content-Type': image.type
                }
            });

            const postContent = {
                contentDescription: content,
                authorName: 'someone',
                mediaUrl: fileName
            };

            const { data: createdPost } = await axios.post('http://localhost:8080/posts', postContent);
            console.log("Created Post:", createdPost);

            setContent('');
            setImage(null);
            setPreview(placeholder);
            setSuccessOpen(true);
            onClose();

        } catch (error) {
            console.error('Error creating post:', error);
            alert('There was an error creating the post.');
        }
    };


    const handleCloseSuccess = () => {
        setSuccessOpen(false);
    };

    return (
        <>
            <Modal
                open={open}
                onClose={onClose}
                closeAfterTransition
                BackdropComponent={Backdrop}
                BackdropProps={{
                    timeout: 500,
                    style: { backgroundColor: 'rgba(0, 0, 0, 0.5)' },
                }}
            >
                <Box
                    sx={{
                        position: 'absolute',
                        top: '50%',
                        left: '50%',
                        transform: 'translate(-50%, -50%)',
                        width: 750,
                        bgcolor: 'background.paper',
                        boxShadow: 24,
                        borderRadius: 4,
                        p: 4,
                    }}
                >
                    <Card>
                        <CardContent>
                            <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                                <Avatar alt="OnlyPans User" src={user} />
                                <Typography variant="body2" color="text.primary" sx={{ ml: 2 }}>
                                    Create a new post
                                </Typography>
                            </Box>

                            <CardMedia
                                component="img"
                                height="400"
                                image={preview}
                                alt="preview"
                                sx={{ mb: 2 }}
                            />

                            <Button
                                variant="contained"
                                component="label"
                                sx={{ mb: 2 }}
                            >
                                Upload Image
                                <input
                                    type="file"
                                    accept="image/*"
                                    onChange={handleImageChange}
                                    hidden
                                />
                            </Button>

                            <Box component="form" onSubmit={handleSubmit}>
                                <TextField
                                    label="Description"
                                    variant="outlined"
                                    fullWidth
                                    multiline
                                    rows={4}
                                    value={content}
                                    onChange={(e) => setContent(e.target.value)}
                                    sx={{ mb: 2 }}
                                />

                                <Box sx={{ display: 'flex', justifyContent: 'flex-end' }}>
                                    <Button type="submit" variant="contained" color="primary">
                                        Post
                                    </Button>
                                </Box>
                            </Box>
                        </CardContent>
                    </Card>
                </Box>
            </Modal>


            <Snackbar
                open={successOpen}
                autoHideDuration={3000}
                onClose={handleCloseSuccess}
                anchorOrigin={{ vertical: 'top', horizontal: 'center' }}
            >
                <Alert onClose={handleCloseSuccess} severity="success" sx={{ width: '100%' }}>
                    Post created successfully!
                </Alert>
            </Snackbar>
        </>
    );
}

export default CreatePost;
