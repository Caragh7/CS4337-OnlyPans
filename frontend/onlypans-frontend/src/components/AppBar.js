import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import IconButton from '@mui/material/IconButton';
import Typography from '@mui/material/Typography';
import Menu from '@mui/material/Menu';
import MenuIcon from '@mui/icons-material/Menu';
import Container from '@mui/material/Container';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import Tooltip from '@mui/material/Tooltip';
import MenuItem from '@mui/material/MenuItem';
import logo from '../assets/textOnly.png';
import user from '../assets/user.png';
import HomeIcon from "@mui/icons-material/Home";
import { Link } from "react-router-dom";
import {useNavigate} from "react-router-dom";
import {useContext} from "react";
import {KeycloakContext} from "./KeyCloakContext";
import { useLocation } from "react-router-dom";



const pages = ['']; // Placeholder
const settings = ['Profile', 'Logout', 'Upgrade'];

function ResponsiveAppBar({ onToggleCreatePost, isCreator }) {
    const [anchorElNav, setAnchorElNav] = React.useState(null);
    const [anchorElUser, setAnchorElUser] = React.useState(null);
    const { keycloak } = useContext(KeycloakContext);
    const navigate = useNavigate();
    const location = useLocation();

    const enableCreatePost = location.pathname === "/posts" || location.pathname === "/feed" && isCreator;

    const handleProfileClick = () => {
        handleCloseUserMenu(); // Close the dropdown
        navigate('/profile'); // Navigate to the UserProfile route
    };

    const handleOpenNavMenu = (event) => {
        setAnchorElNav(event.currentTarget);
    };
    const handleOpenUserMenu = (event) => {
        setAnchorElUser(event.currentTarget);
    };

    const handleCloseNavMenu = () => {
        setAnchorElNav(null);
    };

    const handleCloseUserMenu = () => {
        setAnchorElUser(null);
    };

    return (
        <AppBar position="static" sx={{ backgroundColor: '#fff' }}>
            <Container maxWidth="xl">
                <Toolbar disableGutters>
                    <Box
                        sx={{
                            display: "flex",
                            alignItems: "center",
                            flexGrow: 1,
                            justifyContent: "flex-start",
                        }}
                    >
                        <img
                            src={logo}
                            alt="OnlyPans"
                            style={{ height: "40px", marginRight: "10px" }}
                        />

                        {/* Home Button */}
                        <IconButton
                            component={Link}
                            to="/" // navigate to homepage
                            sx={{
                                color: "#007BFF",
                                backgroundColor: "white",
                                borderRadius: "50%",
                                padding: "5px",
                                "&:hover": {
                                    backgroundColor: "#0056b3",
                                    color: "white",
                                },
                            }}
                        >
                            <HomeIcon />
                        </IconButton>
                        <Typography
                            variant="h6"
                            noWrap
                            component="a"
                            href="#app-bar-with-responsive-menu"
                            sx={{
                                display: { xs: "none", md: "flex" }, // Hide text on small screens
                                fontFamily: "monospace",
                                fontWeight: 700,
                                letterSpacing: ".3rem",
                                color: "inherit",
                                textDecoration: "none",
                            }}
                        >
                            OnlyPans
                        </Typography>
                    </Box>

                    <Box sx={{ flexGrow: 1, display: { xs: 'flex', md: 'none' } }}>
                        {/*<IconButton*/}
                        {/*    size="large"*/}
                        {/*    aria-label="account of current user"*/}
                        {/*    aria-controls="menu-appbar"*/}
                        {/*    aria-haspopup="true"*/}
                        {/*    onClick={handleOpenNavMenu}*/}
                        {/*    color="inherit"*/}
                        {/*>*/}
                            <MenuIcon />
                        {/*</IconButton>*/}
                        <Menu
                            id="nav-menu-appbar"
                            anchorEl={anchorElNav}
                            anchorOrigin={{
                                vertical: 'bottom',
                                horizontal: 'left',
                            }}
                            keepMounted
                            transformOrigin={{
                                vertical: 'top',
                                horizontal: 'left',
                            }}
                            open={Boolean(anchorElNav)}
                            onClose={handleCloseNavMenu}
                            disableAutoFocus // prevents focus conflicts
                            disableRestoreFocus // prevents restoring focus to a hidden element in menu?
                            sx={{ display: { xs: 'block', md: 'none' } }}
                        >
                            {pages.map((page) => (
                                <MenuItem key={page} onClick={handleCloseNavMenu}>
                                    <Typography sx={{ textAlign: 'center' }}>{page}</Typography>
                                </MenuItem>
                            ))}
                        </Menu>
                    </Box>

                    <Box sx={{ flexGrow: 1, display: { xs: 'none', md: 'flex' } }}>
                        {pages.map((page) => (
                            <Button
                                key={page}
                                onClick={handleCloseNavMenu}
                                sx={{ my: 2, color: 'black', display: 'block' }}
                            >
                                {page}
                            </Button>
                        ))}
                    </Box>
              <>
              { enableCreatePost && (
                    <Button variant="contained" color="primary" onClick={onToggleCreatePost} sx={{ mr: 2 }}>
                        Create Post
                    </Button>
                  )}
              </>

                    <Box sx={{ flexGrow: 0 }}>
                        <Tooltip title="Open settings">
                            <IconButton onClick={handleOpenUserMenu} sx={{ p: 0 }}>
                                <Avatar alt="User Profile Picture" src={user} />
                            </IconButton>
                        </Tooltip>
                        <Menu
                            sx={{ mt: '45px' }}
                            id="settings-menu-appbar"
                            anchorEl={anchorElUser}
                            anchorOrigin={{
                                vertical: 'top',
                                horizontal: 'right',
                            }}
                            keepMounted
                            transformOrigin={{
                                vertical: 'top',
                                horizontal: 'right',
                            }}
                            open={Boolean(anchorElUser)}
                            onClose={handleCloseUserMenu}
                        >
                            {settings.map((setting) => (
                                <MenuItem
                                    key={setting}
                                    onClick={() => {
                                        handleCloseUserMenu();
                                        if (setting === 'Profile'){
                                        handleProfileClick();
                                        } else if ( setting === 'Logout') {
                                            keycloak.logout();
                                        } else if (setting === 'Upgrade') {
                                            navigate('/upgrade')
                                        }
                                    }}
                                    >
                                    <Typography sx={{ textAlign: 'center' }}>{setting}</Typography>
                                </MenuItem>
                            ))}
                        </Menu>
                    </Box>
                </Toolbar>
            </Container>
        </AppBar>
    );
}
export default ResponsiveAppBar;
