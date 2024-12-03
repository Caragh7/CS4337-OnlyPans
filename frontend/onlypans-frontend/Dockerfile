# Use the official Node.js 18 image as the base
FROM node:18

# Set the working directory inside the container
WORKDIR /app

# Copy package.json and package-lock.json
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application code
COPY .. .

# Expose port 3000 (the default port for Create React App)
EXPOSE 3000

# Start the app in development mode
CMD ["npm", "start"]
