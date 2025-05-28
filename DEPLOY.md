# Deployment Instructions

## Overall Prerequisites

Before you begin, ensure you have the following software installed on your deployment server:

- **Java Development Kit (JDK):** Version 17 or later (for building the backend).
- **Node.js:** Version 16.17.0 (as recommended in the main `README.md` for building the frontend). You can use a Node version manager like `nvm` to install specific versions.
- **pnpm:** Version 8.12.0 (as recommended in the main `README.md` for frontend dependency management). Install it globally after installing Node.js (e.g., `npm install -g pnpm`).
- **Docker:** For running the applications in containers.
- **Maven:** (Typically included with Java IDEs or can be installed separately) for building the backend Java project.

---

Scroll down for specific instructions for the backend and frontend components.

## Backend Deployment

The backend is a Spring Boot application packaged as a JAR file and run inside a Docker container.

### Prerequisites

- Java 17 or later (for building the JAR)
- Docker

### 1. Build the JAR file

Navigate to the `blog-backend` directory and use Maven to build the project:

```bash
cd blog-backend
mvn clean package
```

This will generate a JAR file in the `blog-backend/target` directory (e.g., `blog-backend-0.0.1-SNAPSHOT.jar`). Copy this JAR file to the `blog-backend` directory (where the Dockerfile is located) or update the `COPY` command in the `blog-backend/Dockerfile` to point to the correct JAR file path if you prefer to keep it in the `target` folder.

*Self-correction: The Dockerfile expects the JAR in `/app`, and the `COPY` command is `COPY blog-backend-0.0.1-SNAPSHOT.jar /app/app.jar`. This implies the JAR should be in the same directory as the Dockerfile when building the image. So, ensure the JAR (e.g., `blog-backend-0.0.1-SNAPSHOT.jar`) is in the `blog-backend` directory before building the Docker image.*


### 2. Build the Docker Image

Navigate to the `blog-backend` directory (if you're not already there) and run the following command to build the Docker image:

```bash
docker build -t blog-backend .
```

### 3. Run the Docker Container

Run the Docker container with the following command, mapping port 8088 in the container to a port on your host machine (e.g., 8088):

```bash
docker run -d -p 8088:8088 --name blog-backend-container blog-backend
```

The backend application should now be accessible on `http://localhost:8088` (or the port you configured).

---

## Frontend Deployment

The frontend consists of two Vue.js applications: `kuailemao-admin` (for the admin panel) and `kuailemao-blog` (for the public blog). Both are served using Nginx and run inside Docker containers.

### Prerequisites

- Node.js (version specified in `README.md` is 16.17.0)
- pnpm (version specified in `README.md` is 8.12.0)
- Docker

### 1. Deploying the Admin Frontend (`kuailemao-admin`)

#### a. Build the Vue.js Application

Navigate to the `blog-frontend/kuailemao-admin` directory and install dependencies using pnpm, then build the application:

```bash
cd blog-frontend/kuailemao-admin
pnpm install
pnpm run build
```

This will generate a `dist/` directory containing the static assets.

#### b. Build the Docker Image

Navigate to the `blog-frontend/kuailemao-admin` directory (if you're not already there) and run the following command:

```bash
docker build -t kuailemao-admin-frontend .
```

#### c. Run the Docker Container

Run the Docker container, mapping port 80 in the container to a port on your host (e.g., 8081 for the admin panel):

```bash
docker run -d -p 8081:80 --name kuailemao-admin-container kuailemao-admin-frontend
```

The admin panel should now be accessible on `http://localhost:8081` (or the port you configured).

### 2. Deploying the Blog Frontend (`kuailemao-blog`)

#### a. Build the Vue.js Application

Navigate to the `blog-frontend/kuailemao-blog` directory and install dependencies using pnpm, then build the application:

```bash
cd blog-frontend/kuailemao-blog
pnpm install
pnpm run build
```

This will generate a `dist/` directory.

#### b. Build the Docker Image

Navigate to the `blog-frontend/kuailemao-blog` directory (if you're not already there) and run the following command:

```bash
docker build -t kuailemao-blog-frontend .
```

#### c. Run the Docker Container

Run the Docker container, mapping port 80 in the container to a port on your host (e.g., 8080 for the public blog):

```bash
docker run -d -p 8080:80 --name kuailemao-blog-container kuailemao-blog-frontend
```

The blog should now be accessible on `http://localhost:8080` (or the port you configured).

**Note:** Ensure the backend is running and accessible to the frontend applications. You might need to configure the API endpoints in the frontend applications to point to the correct backend URL, especially if deploying to different hosts or using different ports than the defaults. Check the `.env` or configuration files within each frontend project for API endpoint settings. The `README.md` also mentions `default.conf` files in each frontend directory, which are used in the Docker images to configure Nginx. These might also need adjustments depending on your server setup (e.g., for reverse proxy configurations).

---
