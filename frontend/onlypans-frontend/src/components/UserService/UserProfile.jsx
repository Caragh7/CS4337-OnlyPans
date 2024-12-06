import { updateUser } from '../../api/UserServiceApi'
import {useContext, useEffect, useState} from "react";

const UserProfile = ({keycloak, authenticated, user}) => {
    const [userData, setUserData] = useState(user || {});
    const [isEditing, setIsEditing] = useState(false);


    const handleUpdate = async () => {
        try {
            const token = keycloak?.token;
            if (!token) {
                console.error("Token is missing. User might not be logged in.");
                return;
            }

            const updatedUser = await updateUser(user.id, userData, token);
            console.log(updatedUser)
            console.log(token)
            setUserData(updatedUser);


            setIsEditing(false);
        } catch (error) {
            console.error("Error updating user:", error);
            alert("Failed to update user. Please try again later.");
        }
    };


    return (
        <div style={{
            maxWidth: "600px",
            margin: "20px auto",
            padding: "20px",
            border: "1px solid #ccc",
            borderRadius: "10px",
            backgroundColor: "#f9f9f9",
            boxShadow: "0 4px 8px rgba(0, 0, 0, 0.1)"
        }}>
            <h1 style={{
                fontSize: "24px",
                fontWeight: "bold",
                marginBottom: "20px",
                textAlign: "center",
                color: "#333"
            }}>
                Your Info
            </h1>
            {!isEditing ? (
                <div style={{lineHeight: "1.6", color: "#555"}}>
                    <p><strong style={{color: "#000"}}>Username:</strong> {userData.username || "N/A"}</p>
                    <p><strong style={{color: "#000"}}>First Name:</strong> {userData.firstName || "N/A"}</p>
                    <p><strong style={{color: "#000"}}>Last Name:</strong> {userData.lastName || "N/A"}</p>
                    <p><strong style={{color: "#000"}}>Email:</strong> {userData.email || "N/A"}</p>
                    {/*<button*/}
                    {/*    onClick={() => setIsEditing(true)}*/}
                    {/*    style={{*/}
                    {/*        marginTop: "15px",*/}
                    {/*        padding: "10px 15px",*/}
                    {/*        backgroundColor: "#007bff",*/}
                    {/*        color: "#fff",*/}
                    {/*        border: "none",*/}
                    {/*        borderRadius: "5px",*/}
                    {/*        cursor: "pointer",*/}
                    {/*        fontWeight: "bold",*/}
                    {/*    }}*/}
                    {/*>*/}
                    {/*    Edit*/}
                    {/*</button>*/}
                </div>
            ) : (
                <div>
                    <label>
                        <strong>First Name:</strong>
                        <input
                            type="text"
                            value={userData.firstName || ""}
                            onChange={(e) => setUserData({...userData, firstName: e.target.value})}
                            placeholder="First name..."
                        />
                    </label>
                    <label>
                        <strong>Last Name:</strong>
                        <input
                            type="text"
                            value={userData.lastName || ""}
                            onChange={(e) => setUserData({...userData, lastName: e.target.value})}
                            placeholder="Last name..."
                        />
                    </label>
                    <label>
                        <strong>User Name:</strong>
                        <input
                            type="text"
                            value={userData.username || ""}
                            onChange={(e) => setUserData({...userData, username: e.target.value})}
                            placeholder="Username..."
                        />
                    </label>


                    <button onClick={handleUpdate}>Save</button>
                    <button
                        onClick={() => {
                            setIsEditing(false); // exiting edit mode
                            setUserData(user); //resetting userData to original props if edits are canceled
                        }}
                    >
                        Cancel
                    </button>
                </div>
            )}
        </div>
    );

};
export default UserProfile;
