# 使用官方的OpenJDK 17镜像作为基础镜像
FROM openjdk:17

# 设置工作目录
WORKDIR /app

# 复制项目的jar文件到容器中
COPY blog-backend-0.0.1-SNAPSHOT.jar /app/app.jar

# 暴露应用运行的端口
EXPOSE 8088

# 运行Spring Boot应用
ENTRYPOINT ["java", "-jar", "/app/app.jar"]