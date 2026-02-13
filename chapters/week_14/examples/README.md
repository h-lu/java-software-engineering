# Week 14 示例代码索引

> 本目录包含 Week 14（发布与部署）的所有示例代码和配置文件。

## 文件结构

```
examples/
├── README.md                           # 本文件（示例索引）
├── semantic-versioning.md              # Semantic Versioning 规范说明
├── pom-shade-plugin.xml                # Maven Shade Plugin 完整配置
├── config-management.md                # 环境配置管理方案对比
├── deployment-strategies.md            # 三种部署方案对比
└── campusflow-release-example/         # CampusFlow 发布示例
    ├── README.md                       # CampusFlow 发布说明
    ├── pom.xml                         # Maven 配置（含 Shade Plugin）
    ├── Dockerfile                      # Docker 镜像配置
    ├── .dockerignore                   # Docker 构建忽略文件
    ├── config-dev.properties           # 开发环境配置
    ├── config-prod.properties          # 生产环境配置
    ├── start.sh                        # Linux/Mac 启动脚本
    ├── start.bat                       # Windows 启动脚本
    ├── Config.java                     # 配置管理类示例
    ├── Main.java                       # 主类示例
    └── src/
        └── main/
            ├── java/com/campusflow/
            │   ├── model/
            │   │   └── Task.java       # 任务实体类
            │   ├── repository/
            │   │   └── TaskRepository.java  # 任务数据访问层
            │   └── api/
            │       └── TaskApi.java    # 任务 REST API
            └── resources/
                ├── config-dev.properties   # 开发环境配置
                └── config-prod.properties  # 生产环境配置
```

## 示例文件说明

### 1. semantic-versioning.md

**内容**：Semantic Versioning 2.0.0 规范说明

**包含**：
- 版本号格式（MAJOR.MINOR.PATCH）
- 版本号决策树
- CampusFlow 版本历史示例
- Git Tag 使用示例
- Commit Message 规范

**适用场景**：学习如何管理软件版本号

---

### 2. pom-shade-plugin.xml

**内容**：Maven Shade Plugin 完整配置

**包含**：
- Maven Shade Plugin 配置
- 主类设置（ManifestResourceTransformer）
- 签名文件排除配置
- 依赖配置（Javalin、Gson、SQLite JDBC）

**适用场景**：学习如何打包可执行 JAR

**运行方式**：
```bash
mvn clean package
java -jar target/campusflow-1.0.0.jar
```

---

### 3. config-management.md

**内容**：三种环境配置方案对比

**包含**：
- 方案 1：配置文件 + 环境变量（推荐新手）
- 方案 2：12-Factor App（纯环境变量）
- 方案 3：配置中心（大型项目）
- Config 类实现示例
- .gitignore 配置建议

**适用场景**：学习如何管理多环境配置

---

### 4. deployment-strategies.md

**内容**：三种部署方案对比

**包含**：
- 方案 1：云平台一键部署（Railway、Render、Fly.io）
- 方案 2：Docker 容器部署
- 方案 3：传统 VPS 部署（AWS EC2、阿里云 ECS）
- Dockerfile 示例
- systemd 服务配置
- Nginx 反向代理配置

**适用场景**：学习如何部署 Java Web 应用

---

### 5. campusflow-release-example/

**内容**：完整的 CampusFlow 发布示例

**包含**：
- Maven 项目配置（pom.xml）
- 配置文件（config-dev.properties、config-prod.properties）
- Java 源代码（Main.java、Config.java、Task.java、TaskRepository.java、TaskApi.java）
- 启动脚本（start.sh、start.bat）
- Docker 配置（Dockerfile、.dockerignore）

**运行方式**：
```bash
# 打包
mvn clean package

# 运行（开发环境）
java -jar target/campusflow-1.0.0.jar

# 运行（生产环境）
CAMPUSFLOW_ENV=prod java -jar target/campusflow-1.0.0.jar
```

## 学习路径

### 第 1 步：理解版本管理

阅读 `semantic-versioning.md`，学习：
- Semantic Versioning 规则
- Git Tag 使用
- Commit Message 规范

### 第 2 步：学习构建打包

查看 `pom-shade-plugin.xml`，学习：
- Maven Shade Plugin 配置
- 如何生成可执行 JAR
- 如何设置主类

### 第 3 步：学习环境配置

阅读 `config-management.md`，学习：
- 三种配置方案对比
- Config 类实现
- .gitignore 配置

### 第 4 步：学习部署策略

阅读 `deployment-strategies.md`，学习：
- 云平台一键部署
- Docker 容器部署
- VPS 部署

### 第 5 步：实践 CampusFlow 发布

查看 `campusflow-release-example/`，实践：
- 打包 CampusFlow 为可执行 JAR
- 配置开发和生产环境
- 部署到云平台（Railway/Render）
- 容器化部署（Docker）

## 常见问题

### Q: 如何运行 CampusFlow 示例？

**A**:
```bash
cd campusflow-release-example
mvn clean package
java -jar target/campusflow-1.0.0.jar
```

### Q: 如何切换到生产环境？

**A**:
```bash
CAMPUSFLOW_ENV=prod java -jar target/campusflow-1.0.0.jar
```

### Q: 如何部署到 Railway？

**A**:
1. 将代码推送到 GitHub
2. 登录 Railway，连接 GitHub 仓库
3. Railway 自动检测 Maven 项目并部署
4. 设置环境变量：`DB_PATH=/var/data/campusflow.db`

### Q: 如何使用 Docker 部署？

**A**:
```bash
cd campusflow-release-example
docker build -t campusflow:1.0.0 .
docker run -d -p 8080:8080 -e DB_PATH=/data/campusflow.db campusflow:1.0.0
```

## 参考资料

- [Semantic Versioning 2.0.0](https://semver.org/)
- [Maven Shade Plugin 文档](https://maven.apache.org/plugins/maven-shade-plugin/)
- [12-Factor App](https://12factor.net/config)
- [Railway 文档](https://docs.railway.app/)
- [Docker 文档](https://docs.docker.com/)
