# CampusFlow 发布示例

> 本示例展示如何将 CampusFlow 从"本地能跑"转变为"别人能用"的可部署应用。

## 本周改进

1. **版本管理**：遵循 Semantic Versioning，打上 v1.0.0 标签
2. **构建打包**：配置 maven-shade-plugin，生成可执行 JAR
3. **环境配置**：支持开发/生产环境配置分离
4. **部署就绪**：提供 Railway/Render/Docker 部署配置

## Git 标签示例

```bash
# 确保代码已提交
git status
git add .
git commit -m "Release v1.0.0: Initial public release"

# 打标签
git tag -a v1.0.0 -m "Release v1.0.0

Features:
- Task management (create, edit, delete)
- Mark tasks as completed
- RESTful API with OpenAPI documentation
- SQLite persistence
- Environment-based configuration

Deployment:
- Packaged as executable JAR
- Deployed to Railway"

# 推送标签
git push origin main --tags

# 验证标签
git show v1.0.0
```

## 版本决策示例

| 变更类型 | 版本升级 | 示例 |
|----------|----------|------|
| 修复 bug | PATCH | 1.0.0 → 1.0.1 |
| 新增功能 | MINOR | 1.0.0 → 1.1.0 |
| API 变更 | MAJOR | 1.0.0 → 2.0.0 |

## 构建和运行

```bash
# 清理之前的构建
mvn clean

# 打包（会运行测试）
mvn package

# 生成的 JAR 在 target/ 目录
ls -lh target/*.jar

# 运行 JAR（开发环境）
java -jar target/campusflow-1.0.0.jar

# 运行 JAR（生产环境）
CAMPUSFLOW_ENV=prod java -jar target/campusflow-1.0.0.jar
```

## 环境配置

### 开发环境

**config-dev.properties**:
```properties
db.path=campusflow.db
server.port=8080
log.level=DEBUG
api.base_path=/api
```

### 生产环境

**config-prod.properties**:
```properties
db.path=/var/data/campusflow.db
server.port=80
log.level=WARN
api.base_path=/api
```

## 部署到 Railway

1. 连接 GitHub 仓库
2. Railway 自动检测 Maven 项目
3. 设置环境变量：
   ```
   DB_PATH=/var/data/campusflow.db
   CAMPUSFLOW_ENV=prod
   ```
4. 自动部署，获取 URL：`https://campusflow.up.railway.app`

## 文件结构

```
campusflow-release-example/
├── pom.xml                           # Maven 配置（含 maven-shade-plugin）
├── config-dev.properties             # 开发环境配置
├── config-prod.properties            # 生产环境配置
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── campusflow/
│       │           ├── Main.java
│       │           ├── config/
│       │           │   └── Config.java
│       │           ├── model/
│       │           ├── repository/
│       │           └── api/
│       └── resources/
│           ├── config-dev.properties
│           └── config-prod.properties
└── README.md
```

## 启动脚本

**start.sh**（Linux/Mac）:
```bash
#!/bin/bash
CAMPUSFLOW_ENV=prod java -jar campusflow-1.0.0.jar
```

**start.bat**（Windows）:
```batch
@echo off
set CAMPUSFLOW_ENV=prod
java -jar campusflow-1.0.0.jar
```
