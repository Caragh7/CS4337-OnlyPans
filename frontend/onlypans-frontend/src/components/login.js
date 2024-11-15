import React from 'react';
import onlypans_alt from '../assets/alt_white.png'
import google from '../assets/icons/google.png'

function Login() {
    return (
        <div style={styles.container}>
            <div style={styles.branding}>
                <img src={onlypans_alt} alt="logo" style={styles.logo}/>
                <b>Please sign up..</b>
            </div>

            <div style={styles.login_flow}>
                <button style={styles.button}>
                    <img src={google} alt="logo" style={styles.google_icon}/>
                </button>

            </div>
        </div>
    );
}

const styles = {
    container: {
        display: 'flex',
        height: '100vh',
        width: '100vw',
    },
    branding: {
        flex: 1,
        backgroundColor: '#00aff0',
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: 'center',
        color: 'white',
        padding: '50px',
    },
    login_flow: {
        flex: 1,
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        padding: '50px',
        backgroundColor: 'white',
    },
    button: {
        padding: '15px 30px',
        fontSize: '1rem',
        border: '1px solid #ccc',
        borderRadius: '5px',
        backgroundColor: 'white',
        cursor: 'pointer',
    },
    logo: {
        width: '50%',
        opacity: '70%',
    },
    google_icon: {
        width: '24px'
    }
};

export default Login;
