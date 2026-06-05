FROM eclipse-temurin:17-jre
WORKDIR /app
COPY target/blog-backend-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 8088
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
