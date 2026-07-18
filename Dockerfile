# =========================================================
# 阶段一：依赖缓存阶段（只要 pom.xml 没变，这里永远读取缓存）
# =========================================================
FROM maven:3.9.6-eclipse-temurin-21 AS deps
WORKDIR /app

# 🚀 绝招：只复制 pom.xml，并强行让 maven 只下载依赖
COPY pom.xml .
RUN mvn dependency:go-offline -B

# =========================================================
# 阶段二：源码编译阶段（每次提交代码，只编译变动的业务 Java 文件）
# =========================================================
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# 从上面的阶段直接把下载好的本地仓库依赖复制过来
COPY --from=deps /root/.m2 /root/.m2
COPY --from=deps /app/pom.xml .

# 复制你本地最新的 src 业务代码并进行闪电打包
COPY src ./src
RUN mvn clean package -DskipTests

# =========================================================
# 阶段三：轻量级运行阶段（保持不变）
# =========================================================
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

ENV JAVA_OPTS="-Xms256m -Xmx384m -Dfile.encoding=UTF-8"
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]
