import React, { useState } from "react";
import {
    Box,
    Typography,
    TextField,
    Button,
    CircularProgress,
    Alert,
} from "@mui/material";
import { upgradeToCreatorProfileReq } from "../api/UserServiceApi";
import { deleteCreatorProfile } from "../api/CreatorServiceApi";

const UpgradeToCreatorProfile = ({ keycloak, authenticated, user, isCreator, setIsCreator }) => {
    const [status, setStatus] = useState("");
    const [price, setPrice] = useState("");
    const [isValidPrice, setIsValidPrice] = useState(false);
    const [loading, setLoading] = useState(false);

    const token = keycloak?.token;

    const handlePriceChange = (event) => {
        const inputPrice = event.target.value;
        setPrice(inputPrice);
        setIsValidPrice(inputPrice && !isNaN(inputPrice) && Number(inputPrice) > 0);
    };

    const handleToggleCreatorStatus = async () => {
        if (loading) return;
        setLoading(true);

        if (isCreator) {
            try {
                await deleteCreatorProfile(user.id, token);
                setStatus("Creator profile removed successfully.");
                setIsCreator(false);
            } catch {
                setStatus("Failed to remove creator profile. Please try again.");
            }
        } else {
            if (!isValidPrice) {
                setStatus("Please enter a valid price before upgrading.");
                setLoading(false);
                return;
            }

            try {
                const response = await upgradeToCreatorProfileReq(user.id, token, price);
                setStatus(response || "Profile upgraded successfully.");
                setIsCreator(true);
            } catch {
                setStatus("Failed to upgrade user. Please try again.");
            }
        }

        setLoading(false);
    };

    if (!authenticated) {
        return (
            <Box display="flex" justifyContent="center" alignItems="center" height="100vh">
                <Typography variant="h6">You must be logged in to view this page.</Typography>
            </Box>
        );
    }

    return (
        <Box
            maxWidth={500}
            mx="auto"
            mt={5}
            p={3}
            borderRadius={2}
            boxShadow={3}
            textAlign="center"
        >
            <Typography variant="h4" gutterBottom>
                {isCreator ? "Remove Creator Profile" : "Upgrade to Creator Profile"}
            </Typography>
            <Typography variant="body1" gutterBottom>
                Click the button below to {isCreator ? "remove" : "upgrade"} your profile.
            </Typography>
            {!isCreator && (
                <Box mb={3}>
                    <TextField
                        label="Set Your Price"
                        variant="outlined"
                        fullWidth
                        value={price}
                        onChange={handlePriceChange}
                        helperText={
                            isValidPrice
                                ? "Price looks good!"
                                : "Enter a valid price greater than 0."
                        }
                        error={!isValidPrice && price !== ""}
                    />
                </Box>
            )}
            <Button
                variant="contained"
                color={isCreator ? "error" : "success"}
                onClick={handleToggleCreatorStatus}
                disabled={!isCreator && (!isValidPrice || loading)}
                sx={{ textTransform: "none", fontSize: "16px", minWidth: 200 }}
            >
                {loading ? (
                    <CircularProgress size={24} sx={{ color: "#fff" }} />
                ) : isCreator ? (
                    "Undo Upgrade"
                ) : (
                    "Upgrade to Creator"
                )}
            </Button>
            {status && (
                <Box mt={3}>
                    <Alert severity={status.includes("success") ? "success" : "error"}>
                        {status}
                    </Alert>
                </Box>
            )}
        </Box>
    );
};

export default UpgradeToCreatorProfile;
