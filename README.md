## **OnlyPans**

---

### **Project Overview**
A microservice-based web application that allows users to subscribe to creators, view posts, and manage their profiles. Built with Spring Boot and following Agile processes, the project integrates server-to-server communication, MySQL database interactions, and OAuth for third-party integrations.

---

### **Key Features**
- **Microservices**:
    - Subscription Service
    - User Service
    - Creator Service
    - Post Service
    - Media Service
    - API Gateway
- **Database**:
    - MySQL with replication support.
- **Authentication**:
    - Keycloak for OAuth and user management.
- **Frontend**:
    - React.js for the user interface.
- **DevOps**:
    - CI/CD with GitHub Actions.
    - Docker Compose for containerized deployment.
- **Testing**:
    - Unit, integration, and load testing.

---

### **System Architecture**
- **Frontend**: React.js with Next.js.
- **Backend**: Spring Boot microservices.
- **Communication**: REST APIs.
- **Database**: MySQL for data storage.
- **Authentication**: Keycloak with pre-configured realms.

---

### **Deployment**
The project uses Docker Compose for local development and deployment. Services are networked using a dynamic IP overlay network.

- Deployed frontend live **[here](https://uni-onlypans-frontend.kobos.studio)**
- Deployed keycloak live **[here](https://uni-onlypans-keycloak.kobos.studio)**
- Deployed API live  **[here](https://uni-onlypans-frontend.kobos.studio/api)**
- Deployed Consul live **[here](https://uni-onlypans-frontend.kobos.studio/consul)**

---

### **Quickstart**

1. **Clone the Repository**
   ```bash
   git clone https://github.com/Caragh7/CS4337-OnlyPans.git
   cd CS4337-Onlypans-main
   ```

2. **Environment Setup**
    - Copy the secrets `./configs/*` to `./configs/`.

3. **Build and Run with Docker Compose**
   ```bash
   docker compose up -d
   ```

4. **Access the Application**
    - Frontend: `http://localhost:3000`
    - Keycloak Admin Console: `http://localhost:8081`

---

### **Technology Stack**
- **Frontend**: React, MaterialUI
- **Backend**: Spring Boot (Java)
- **Database**: MySQL
- **Authentication**: Keycloak
- **DevOps**: GitHub Actions, Docker

---

### **Key Functionalities**
- **CRUD Operations**: Implemented for users, creators, subscriptions, posts, and media.
- **Server-to-Server Communication**: RESTful APIs between microservices.
- **OAuth Integration**: Secure third-party API interactions.
- **Fault Tolerance**: MySQL replication and service failover.

---

### **Testing**
1. **Unit Testing**: Ensures isolated functionality works as expected.
2. **Integration Testing**: Validates communication between microservices.
3. **Load Testing**: Simulates stress scenarios to assess system resilience.

---

### **Agile Workflow**
1. **Sprint Duration**: 2 weeks.
2. **Planning**: Managed using [Trello](https://trello.com/b/ihALjVHX/project-progress).
3. **Reporting**: Weekly standup reports and bi-weekly sprint reviews.

---

### **CI/CD Pipeline**
- Automated builds and deployments using GitHub Actions.
- Testing integrated into the pipeline.

---

### **Third-Party Integrations**
- Google APIs for OAuth.
- Stripe API for Payments
- Stripe Webhooks for Payment Validation
- KeyCloak for Session Management

---

### **Team Contributions**

#### **Contributors**
- **Caragh Morahan** – 21340005
- **Martynas Danys** – 21315884
- **Sam Ennis** - 21321302
- **John Walsh** - 20245424

#### **Pull Requests by Contributors**
- **Caragh Morahan (Caragh7)**: [PR Details](https://github.com/Caragh7/CS4337-OnlyPans/pulls?q=is%3Apr+author%3ACaragh7)
- **Martynas Danys (MartyD1)**: [PR Details](https://github.com/Caragh7/CS4337-OnlyPans/pulls?q=is%3Apr+author%3AMartyD1)
- **Sam Ennis (samennis1)**: [PR Details](https://github.com/Caragh7/CS4337-OnlyPans/pulls?q=is%3Apr+author%3Asamennis1)
- **John Walsh (wjohn564)**:, [PR Details](https://github.com/Caragh7/CS4337-OnlyPans/pulls?q=is%3Apr+author%3Awjohn564)

---

### **Important Commands**
- **Run Tests**:
  ```bash
  ./gradlew test
  ```
- **Build JARs**:
  ```bash
  ./gradlew clean build -x test
  ```
- **Restart Docker Services**:
  ```bash
  docker compose restart
  ```

---