
# Build Angular
FROM node:24-alpine AS frontend-build

WORKDIR /frontend

COPY frontend/tidybalance_frontend/package*.json ./
RUN npm ci

COPY frontend/tidybalance_frontend/ ./
RUN npm run build -- --configuration production


# Build Spring Boot
FROM maven:3.9-eclipse-temurin-21 AS backend-build

WORKDIR /app

COPY backend/tidybalance_backend/pom.xml ./
RUN mvn dependency:go-offline

COPY backend/tidybalance_backend/src src

# Copy Angular production build into Spring Boot static resources
COPY --from=frontend-build /frontend/dist/tidybalance_frontend/browser/ \
     /app/src/main/resources/static/

RUN mvn clean package -DskipTests


# Run application
FROM eclipse-temurin:21

WORKDIR /app

COPY --from=backend-build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
