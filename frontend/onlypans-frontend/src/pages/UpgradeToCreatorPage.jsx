import React, { useEffect, useState } from "react";
import axios from "axios";
import { upgradeToCreatorProfileReq } from "../api/UserServiceApi";
import { deleteCreatorProfile, fetchCreatorByUserId } from "../api/CreatorServiceApi";

const UpgradeToCreatorProfile = ({ keycloak, authenticated, user, isCreator, setIsCreator  }) => {
    const [status, setStatus] = useState("");
    const [price, setPrice] = useState(""); // New state for the price input
    const [isValidPrice, setIsValidPrice] = useState(false); // To track if the price is valid

    const token = keycloak?.token;


    const handlePriceChange = (event) => {
        const inputPrice = event.target.value;
        setPrice(inputPrice);
        setIsValidPrice(inputPrice && !isNaN(inputPrice) && Number(inputPrice) > 0); // Validate price
    };

    const handleToggleCreatorStatus = async () => {
        if (isCreator) {
            try {
                await deleteCreatorProfile(user.id, token);
                setStatus("Creator Profile Removed");
                setIsCreator(false);
            } catch (error) {
                setStatus("Failed to remove creator profile. Please try again.");
            }
        } else {
            if (!isValidPrice) {
                setStatus("Please enter a valid price before upgrading.");
                return;
            }

            try {
                const response = await upgradeToCreatorProfileReq(user.id, token, price);
                setStatus(response);
                setIsCreator(true);
            } catch (error) {
                setStatus("Failed to upgrade user. Please try again.");
            }
        }
    };

    if(!authenticated) {
        return <div>You must be logged in to view this page.</div>;
    }

    return (
        <div style={{ textAlign: "center", marginTop: "50px" }}>
            <h1>{isCreator ? "Remove Creator Profile" : "Upgrade to Creator Profile"}</h1>
            <p>Click the button below to {isCreator ? "remove" : "upgrade"} your profile.</p>
            {!isCreator && (
                <div style={{ marginBottom: "20px" }}>
                    <label htmlFor="priceInput" style={{ marginRight: "10px" }}>
                        Set your price:
                    </label>
                    <input
                        id="priceInput"
                        type="text"
                        value={price}
                        onChange={handlePriceChange}
                        placeholder="Enter price"
                        style={{ padding: "5px", borderRadius: "5px", border: "1px solid #ccc" }}
                    />
                </div>
            )}
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
                disabled={!isCreator && !isValidPrice} 
            >
                {isCreator ? "Undo Upgrade" : "Upgrade to Creator"}
            </button>
            {status && (
                <p
                    style={{
                        marginTop: "20px",
                        color: status.includes("success") ? "green" : "red",
                    }}
                >
                    {status}
                </p>
            )}
        </div>
    );
};

export default UpgradeToCreatorProfile;
