import { updateUser } from '../../api/UserServiceApi'
import {useContext, useEffect, useState} from "react";

const UserProfile = ({keycloak, authenticated, user}) => {
    const [userData, setUserData] = useState(user || {});
    const [isEditing, setIsEditing] = useState(false);

    if (!authenticated) {
        console.error("User is not authenticated");
        return <div>You must be logged in to view this page.</div>;
    }

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
        <div>
            <h1>User Profile</h1>
            {!isEditing ? (
                <div>
                    <p><strong>Username:</strong> {userData.username || "N/A"}</p>
                    <p><strong>First Name:</strong> {userData.firstName || "N/A"}</p>
                    <p><strong>Last Name:</strong> {userData.lastName || "N/A"}</p>
                    <p><strong>Email:</strong> {userData.email || "N/A"}</p>
                    <button onClick={() => setIsEditing(true)}>Edit</button>
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
