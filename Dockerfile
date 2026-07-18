# 1. 编译阶段：使用 Maven 镜像编译打包
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# 2. 运行阶段：使用轻量级 JDK21 镜像运行，死抠 512MB 内存
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# 🚀 针对 Render 免费版 512MB 限制，锁死 JVM 内存最大 384M，防止被系统强杀
ENV JAVA_OPTS="-Xms256m -Xmx384m -Dfile.encoding=UTF-8"
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]
