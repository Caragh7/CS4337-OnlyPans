import authApi from './authApi';

export const sendLoginNotification = async (email, token) => {
    try {
        const response = await authApi.post(
            `${process.env.REACT_APP_API_GATEWAY_URL}/email/send`,
            {
                to: email,
                subject: 'OnlyPans Login Notification',
                templateName: 'detected_login',
            },
            {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            }
        );
        console.log('Login notification sent:', response.data);
        return response.data;
    } catch (error) {
        console.error('Failed to send login notification:', error);
        throw error;
    }
};
