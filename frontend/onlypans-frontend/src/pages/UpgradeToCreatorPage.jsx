

import React, { useState } from "react";
import axios from "axios";
import {upgradeToCreatorProfileReq} from "../api/UserServiceApi";
import {deleteCreatorProfile} from "../api/CreatorServiceApi";

const UpgradeToCreatorProfile = ({keycloak, authenticated, user}) => {
    const [status, setStatus] = useState("");
    const [isCreator, setIsCreator] = useState(false);

    if (!authenticated) {
        console.error("User is not authenticated");
        return <div>You must be logged in to view this page.</div>;
    }
    // put in a get request here to see if the user already has a creator profile...

    const handleToggleCreatorStatus = async () => {
        const token = keycloak?.token;
            if (isCreator) {
                // calling creator service to delete the creator profile
                try {
                    const response = deleteCreatorProfile(user.id, token);
                    setStatus("Creator Profile Removed");
                    setIsCreator(false)
                } catch (error) {
                    setStatus("Failed to remove creator profile. Please try again.");
                }

            }else {
                // calling creator service to create a creator profile for the current user
                try {
                    const response = await upgradeToCreatorProfileReq(user.id, token);
                    setStatus(response);
                    setIsCreator(true)
                } catch (error) {
                    setStatus("Failed to upgrade user. Please try again.");
                }
            }
    };

    return (
        <div style={{ textAlign: "center", marginTop: "50px" }}>
            <h1>{isCreator ? "Remove Creator Profile" : "Upgrade to Creator Profile"}</h1>
            <p>Click the button below to upgrade your profile.</p>
            <button
                onClick={handleToggleCreatorStatus}
                style={{
                    padding: "10px 20px",
                    fontSize: "16px",
                    cursor: "pointer",
                    borderRadius: "5px",
                    border: "1px solid #ccc",
                    background: isCreator ? "#FF4136" : "#4CAF50",
                    color: "#fff",
                }}
            >
                {isCreator ? "Undo Upgrade" : "Upgrade to Creator"}
            </button>
            {status && <p style={{ marginTop: "20px", color: status.includes("success") ? "green" : "red"}}>{status}</p>}
        </div>
    );
};

export default UpgradeToCreatorProfile;
