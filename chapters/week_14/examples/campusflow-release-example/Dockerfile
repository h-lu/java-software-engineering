# CampusFlow Docker 镜像
#
# 本例演示：
# 1. 多阶段构建（减小镜像大小）
# 2. 健康检查配置
# 3. 环境变量注入
#
# 构建方式：
# - docker build -t campusflow:1.0.0 .
#
# 运行方式：
# - docker run -d -p 8080:8080 -e DB_PATH=/data/campusflow.db campusflow:1.0.0
#
# 预期输出：
# - Server started on port 8080

# 多阶段构建（减小镜像大小）
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# 复制 pom.xml 并下载依赖（利用 Docker 缓存）
COPY pom.xml .
RUN mvn dependency:go-offline

# 复制源代码并打包
COPY src ./src
RUN mvn clean package -DskipTests

# 运行时镜像（更小）
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# 复制打包好的 JAR
COPY --from=build /app/target/campusflow-1.0.0.jar app.jar

# 暴露端口
EXPOSE 8080

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/api/health || exit 1

# 启动应用
ENTRYPOINT ["java", "-jar", "app.jar"]
