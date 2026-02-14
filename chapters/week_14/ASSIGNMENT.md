# Week 14 作业：发布与部署——从"本地能跑"到"别人能用"

> "代码写完了只是完成了 50%，剩下 50% 是让用户能真正用上。"
> —— 老潘

---

## 作业概述

本周你将完成 CampusFlow 的最后一公里——从"本地开发环境"推向"公网可访问服务"。这是一个里程碑式的转变：室友不再需要克隆代码、配置环境，只需要打开网址就能使用你的应用。

**核心任务**：
1. 使用 Semantic Versioning 给 CampusFlow 打版本标签
2. 使用 Maven 生成可执行 JAR 包
3. 实现环境配置管理（开发/生产环境分离）
4. 部署到云平台，提供公网访问地址

---

## 基础作业（必做）

### 任务 1：版本管理——Semantic Versioning + Git Tag（20 分）

**目标**：给 CampusFlow 打上 v1.0.0 版本标签，遵循 Semantic Versioning 规范。

**背景知识**：

Semantic Versioning（语义化版本）用三段数字传递变更信息：
- **MAJOR**（主版本）：不兼容的 API 变更（如 1.0.0 → 2.0.0）
- **MINOR**（次版本）：向后兼容的新功能（如 1.0.0 → 1.1.0）
- **PATCH**（补丁版本）：向后兼容的问题修复（如 1.0.0 → 1.0.1）

**要求**：

1. **分析 CampusFlow 的变更历史**：
   - 回顾从 Week 01 到 Week 13 的所有功能变更
   - 判断当前版本应该是 v0.x（开发中）还是 v1.0.0（正式发布）
   - 判断标准：功能完整、测试覆盖、文档齐全

2. **创建 Git Tag**：
   ```bash
   # 1. 确保代码已提交
   git status
   git add .
   git commit -m "Release v1.0.0: Initial public release"

   # 2. 创建附注标签（推荐）
   git tag -a v1.0.0 -m "Release v1.0.0

   Features:
   - Task management (create, edit, delete)
   - Mark tasks as completed
   - RESTful API with OpenAPI documentation
   - SQLite persistence

   Deployment:
   - Packaged as executable JAR
   - Deployed to [你的云平台名称]
   "

   # 3. 推送标签到远程仓库
   git push origin main --tags
   ```

3. **编写 CHANGELOG.md**：
   - 记录从 v0.0.1 到 v1.0.0 的所有重要变更
   - 按 Added（新增）、Changed（变更）、Fixed（修复）分类
   - 参考格式：
   ```markdown
   # Changelog

   ## [1.0.0] - 2026-02-15

   ### Added
   - Task CRUD operations (create, read, update, delete)
   - Task completion tracking
   - RESTful API with OpenAPI specification
   - SQLite persistence layer
   - Unit and integration tests

   ### Changed
   - Migrated from in-memory storage to SQLite database
   - Refactored to Repository pattern

   ### Fixed
   - Task title truncation bug
   - Date parsing edge cases
   ```

**提交物**：
- `CHANGELOG.md`：版本变更日志
- Git 标签截图（`git tag -n9` 的输出）

**评分要点**：
- 版本号符合 SemVer 规范（5 分）
- Git tag 使用附注标签（5 分）
- CHANGELOG 结构完整（5 分）
- 变更分类准确（5 分）

**常见错误**：
- ❌ 使用轻量标签（`git tag v1.0.0`）而非附注标签
- ❌ 版本号判断错误（功能不完整就发布 v1.0.0）
- ❌ CHANGELOG 只有一行，没有详细说明

---

### 任务 2：Maven 打包——生成可执行 JAR（25 分）

**目标**：配置 maven-shade-plugin，生成包含所有依赖的可执行 JAR。

**背景知识**：

你之前使用的 `mvn compile exec:java` 只适合开发阶段。要分发软件，需要把所有依赖（Javalin、Gson、SQLite JDBC 等）打包成一个 JAR 文件。用户只需要 Java 就能运行，不需要 Maven、不需要源代码。

**要求**：

1. **配置 pom.xml**：

   在 `<build><plugins>` 中添加 maven-shade-plugin：

   ```xml
   <plugin>
     <groupId>org.apache.maven.plugins</groupId>
     <artifactId>maven-shade-plugin</artifactId>
     <version>3.5.1</version>
     <executions>
       <execution>
         <phase>package</phase>
         <goals>
           <goal>shade</goal>
         </goals>
         <configuration>
           <!-- 设置主类（入口点） -->
           <transformers>
             <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
               <mainClass>com.campusflow.Main</mainClass>
             </transformer>
           </transformers>
           <!-- 排除签名文件（避免冲突） -->
           <filters>
             <filter>
               <artifact>*:*</artifact>
               <excludes>
                 <exclude>META-INF/*.SF</exclude>
                 <exclude>META-INF/*.DSA</exclude>
                 <exclude>META-INF/*.RSA</exclude>
               </excludes>
             </filter>
           </filters>
         </configuration>
       </execution>
     </executions>
   </plugin>
   ```

2. **打包并验证**：

   ```bash
   # 清理之前的构建
   mvn clean

   # 打包（会运行测试）
   mvn package

   # 查看生成的 JAR
   ls -lh target/*.jar

   # 运行 JAR
   java -jar target/campusflow-1.0.0.jar
   ```

3. **验证 JAR 可执行性**：
   - 双击 JAR（或在命令行运行）能正常启动
   - 访问 http://localhost:7070/api/tasks 能正常响应
   - 查看 MANIFEST.MF 确认主类已设置：
   ```bash
   unzip -p target/campusflow-1.0.0.jar META-INF/MANIFEST.MF
   ```

4. **编写启动脚本**（可选，但推荐）：

   **start.sh（Linux/Mac）**：
   ```bash
   #!/bin/bash
   java -jar campusflow-1.0.0.jar
   ```

   **start.bat（Windows）**：
   ```batch
   @echo off
   java -jar campusflow-1.0.0.jar
   ```

   记得给脚本添加执行权限：
   ```bash
   chmod +x start.sh
   ```

**提交物**：
- `pom.xml`：包含 maven-shade-plugin 配置
- `target/campusflow-1.0.0.jar`：可执行 JAR（或截图证明文件存在）
- `MANIFEST.txt`：JAR 的 MANIFEST.MF 内容
- 启动脚本（可选）

**评分要点**：
- maven-shade-plugin 配置正确（8 分）
- JAR 能独立运行（不需要 Maven）（8 分）
- 主类正确设置（5 分）
- 签名文件已排除（4 分）

**常见错误**：
- ❌ 主类配置错误（`mainClass` 写错，导致 "no main manifest attribute"）
- ❌ 没有排除签名文件（打包时报错 "Signature already exists"）
- ❌ JAR 依赖缺失（运行时报 ClassNotFoundException）

---

### 任务 3：环境配置管理（25 分）

**目标**：实现多环境配置管理，让一套代码适应开发/生产环境。

**背景知识**：

开发环境和生产环境的配置差异很大：
- 数据库路径：`campusflow.db`（本地） vs `/var/data/campusflow.db`（服务器）
- 端口：`7070`（本地） vs `80` 或 `8080`（云平台）
- 日志级别：`DEBUG`（开发） vs `WARN`（生产）

硬编码配置会导致"本地能跑、生产崩了"的问题。

**要求**：

1. **创建配置文件**：

   **src/main/resources/config-dev.properties**（开发环境）：
   ```properties
   # 数据库配置
   db.path=campusflow.db

   # 服务器配置
   server.port=7070

   # 日志配置
   log.level=DEBUG
   ```

   **src/main/resources/config-prod.properties**（生产环境）：
   ```properties
   # 数据库配置
   db.path=/var/data/campusflow.db

   # 服务器配置
   server.port=${PORT:8080}

   # 日志配置
   log.level=WARN
   ```

   > **注意**：云平台通常通过环境变量 `PORT` 指定端口，`` `${PORT:8080}` `` 表示"优先使用环境变量 PORT，否则默认 8080"。

2. **创建 Config 类**：

   ```java
   package com.campusflow.config;

   import java.io.IOException;
   import java.io.InputStream;
   import java.util.Properties;

   public class Config {
       private final Properties props;
       private final String env;

       public Config() {
           // 从环境变量读取环境名称（dev/test/prod）
           this.env = System.getenv().getOrDefault("CAMPUSFLOW_ENV", "dev");
           this.props = loadConfig();
       }

       private Properties loadConfig() {
           Properties props = new Properties();
           String configFile = "config-" + env + ".properties";
           try (InputStream input = getClass().getClassLoader().getResourceAsStream(configFile)) {
               if (input == null) {
                   throw new RuntimeException("Config file not found: " + configFile);
               }
               props.load(input);
           } catch (IOException e) {
               throw new RuntimeException("Failed to load config: " + configFile, e);
           }
           return props;
       }

       public String getDbPath() {
           return props.getProperty("db.path");
       }

       public int getPort() {
           String portStr = props.getProperty("server.port");
           // 支持环境变量占位符（如 ${PORT:8080}）
           if (portStr.startsWith("${")) {
               String envVar = portStr.substring(2, portStr.indexOf(':'));
               String defaultValue = portStr.substring(portStr.indexOf(':') + 1, portStr.length() - 1);
               return Integer.parseInt(System.getenv().getOrDefault(envVar, defaultValue));
           }
           return Integer.parseInt(portStr);
       }

       public String getEnv() {
           return env;
       }
   }
   ```

3. **修改 App.java 使用配置**：

   ```java
   // 之前（硬编码）
   var app = Javalin.create().start(7070);

   // 之后（使用配置）
   Config config = new Config();
   var app = Javalin.create().start(config.getPort());
   ```

4. **测试环境切换**：

   ```bash
   # 开发环境（默认）
   java -jar campusflow-1.0.0.jar

   # 生产环境
   CAMPUSFLOW_ENV=prod java -jar campusflow-1.0.0.jar

   # 或者在 PowerShell 中
   $env:CAMPUSFLOW_ENV="prod"; java -jar campusflow-1.0.0.jar
   ```

**安全要求**：

- ❌ **禁止**在配置文件中写入敏感信息（数据库密码、API 密钥）
- ✅ 敏感信息必须通过环境变量注入
- ✅ 配置文件中的敏感信息用占位符表示（如 `` `${DB_PASSWORD}` ``）

**提交物**：
- `config-dev.properties` 和 `config-prod.properties`
- `Config.java`：配置管理类
- `App.java`：修改后的启动类

**评分要点**：
- 配置文件结构合理（7 分）
- Config 类实现正确（10 分）
- 支持环境变量切换（5 分）
- 敏感信息处理正确（3 分）

**常见错误**：
- ❌ 配置文件路径错误（没放在 src/main/resources 下）
- ❌ 环境变量硬编码（如 `if (env == "prod")` 改端口）
- ❌ 敏感信息写进配置文件（如 `db.password=secret123`）

---

### 任务 4：部署到云平台（30 分）

**目标**：将 CampusFlow 部署到云平台，提供公网访问地址。

**推荐平台**（任选一个）：

| 平台 | 推荐度 | 免费额度 | 特点 |
|------|--------|---------|------|
| **Railway**（railway.app） | ⭐⭐⭐⭐⭐ | $5/月免费额度 | 最简单，自动检测 Maven 项目 |
| **Render**（render.com） | ⭐⭐⭐⭐ | 免费（有限制） | 支持 Java，但可能需要休眠 |
| **Fly.io**（fly.io） | ⭐⭐⭐⭐ | 3 个应用免费 | 支持容器，配置灵活 |
| **Vercel** | ⭐⭐⭐ | 有限 | 适合前端，Java 需要额外配置 |

**部署步骤（以 Railway 为例）**：

1. **连接 GitHub**：
   - 登录 [railway.app](https://railway.app)
   - 点击 "New Project" → "Deploy from GitHub repo"
   - 授权 Railway 访问你的 GitHub

2. **选择仓库**：
   - 选择你的 campusflow 仓库
   - Railway 自动检测 Maven 项目

3. **配置环境变量**：
   在 Railway 控制台设置：
   ```
   CAMPUSFLOW_ENV=prod
   PORT=8080（Railway 会自动设置，可以不填）
   ```

4. **自动部署**：
   - Railway 自动运行 `mvn package`
   - 启动服务
   - 分配公网 URL（如 `https://campusflow.up.railway.app`）

5. **验证部署**：
   - 打开分配的 URL
   - 访问 `/api/tasks` 端点
   - 测试创建任务、删除任务

**Fly.io 部署步骤（替代方案）**：

1. **安装 Fly CLI**：
   ```bash
   curl -L https://fly.io/install.sh | sh
   ```

2. **登录**：
   ```bash
   fly auth login
   ```

3. **初始化应用**：
   ```bash
   fly launch
   ```

4. **配置环境变量**：
   ```bash
   fly secrets set CAMPUSFLOW_ENV=prod
   ```

5. **部署**：
   ```bash
   fly deploy
   ```

**验证要求**：

1. **功能测试**：
   - [ ] GET /api/tasks 返回任务列表
   - [ ] POST /api/tasks 能创建任务
   - [ ] DELETE /api/tasks/`{id}` 能删除任务

2. **截图要求**：
   - 浏览器访问部署地址的截图（显示任务列表或欢迎页）
   - 浏览器开发者工具 Network 标签截图（显示 API 请求成功）
   - 云平台控制台截图（显示服务正在运行）

3. **部署说明**：
   - 编写 `DEPLOYMENT.md`，包含：
     - 选择的云平台及理由
     - 部署步骤（简化版）
     - 环境变量配置
     - 访问地址
     - 已知问题（如有）

**提交物**：
- 公网访问地址（URL）
- `DEPLOYMENT.md`：部署说明文档
- 3 张截图（功能验证、Network 标签、控制台）

**评分要点**：
- 服务能正常访问（10 分）
- API 功能完整（10 分）
- 部署文档清晰（5 分）
- 截图完整（5 分）

**常见错误**：
- ❌ 部署成功但 API 返回 500（数据库路径错误）
- ❌ 环境变量未配置（CAMPUSFLOW_ENV=dev 而非 prod）
- ❌ 没有设置端口（云平台分配的端口与应用监听的端口不一致）

---

## 进阶作业（选做，+20 分）

### 进阶 1：Docker 容器化部署（+10 分）

**目标**：编写 Dockerfile，使用 Docker 部署 CampusFlow。

**要求**：

1. **编写多阶段构建 Dockerfile**：
   ```dockerfile
   # 构建阶段
   FROM maven:3.9-eclipse-temurin-17 AS build
   WORKDIR /app
   COPY pom.xml .
   COPY src ./src
   RUN mvn clean package -DskipTests

   # 运行阶段
   FROM eclipse-temurin:17-jre-alpine
   WORKDIR /app
   COPY --from=build /app/target/campusflow-*.jar app.jar
   EXPOSE 8080
   ENTRYPOINT ["java", "-jar", "app.jar"]
   ```

2. **构建并运行**：
   ```bash
   # 构建镜像
   docker build -t campusflow:1.0.0 .

   # 运行容器
   docker run -p 8080:8080 -e CAMPUSFLOW_ENV=prod campusflow:1.0.0
   ```

3. **推送到容器注册表并部署**：
   - 推送到 Docker Hub 或 GitHub Container Registry
   - 使用 Fly.io 或 Railway 部署容器

**提交物**：
- `Dockerfile`
- 部署说明（如何从容器注册表部署）

---

### 进阶 2：多环境部署（+10 分）

**目标**：同时部署开发和生产环境，实现环境隔离。

**要求**：

1. **创建两个独立部署**：
   - 开发环境：campusflow-dev.up.railway.app
   - 生产环境：campusflow-prod.up.railway.app

2. **配置差异**：
   - 开发环境：DEBUG 日志，允许 CORS 任何来源
   - 生产环境：WARN 日志，CORS 限制具体域名

3. **实现蓝绿部署**：
   - 使用 GitHub 分支管理环境（dev 分支 → 开发环境，main 分支 → 生产环境）
   - 配置 Railway/Fly.io 自动部署对应分支

**提交物**：
- 两个环境的访问地址
- 环境配置差异说明

---

### 进阶 3：CI/CD 自动化（+10 分）

**目标**：配置 GitHub Actions，实现自动构建和部署。

**要求**：

1. **创建 GitHub Actions 工作流**：
   ```yaml
   name: Build and Test

   on: [push, pull_request]

   jobs:
     build:
       runs-on: ubuntu-latest
       steps:
         - uses: actions/checkout@v3
         - name: Set up JDK 17
           uses: actions/setup-java@v3
           with:
             java-version: '17'
             distribution: 'temurin'
         - name: Build with Maven
           run: mvn -B package --file pom.xml
         - name: Run tests
           run: mvn test
   ```

2. **配置自动部署**（可选）：
   - 推送到 Railway/Fly.io 的 API
   - 每次推送到 main 分支自动部署

**提交物**：
- `.github/workflows/build.yml`
- GitHub Actions 运行成功的截图

---

## AI 协作练习（可选）

**主题**：审查 AI 生成的部署配置

**目标**：练习审查 AI 生成的 Dockerfile 或部署配置，找出安全和最佳实践问题。

**步骤**：

1. **使用 AI 生成 Dockerfile**：
   ```
   Prompt: "帮我写一个 Java 应用的 Dockerfile，基于 Maven 3.9 和 Java 17，应用入口是 com.campusflow.Main"
   ```

2. **使用审查清单评估**：
   - [ ] 是否使用多阶段构建（减小镜像大小）？
   - [ ] 基础镜像是否安全（有无使用 latest 标签）？
   - [ ] 是否暴露了正确的端口？
   - [ ] 是否设置了健康检查（HEALTHCHECK）？
   - [ ] 敏感信息是否通过环境变量注入？
   - [ ] 是否有安全漏洞（如以 root 用户运行）？

3. **记录发现的问题**：
   - 至少找出 3 个问题
   - 对每个问题给出修复建议

4. **修复并重新生成**：
   - 告诉 AI 发现的问题，让它重新生成
   - 对比两次生成的差异

**提交物**：
- AI 生成的原始 Dockerfile
- 审查报告（列出发现的问题）
- 修复后的 Dockerfile

---

## AI 使用日志（本作业必交）

创建 `AI_LOG.md`，记录你在本作业中使用 AI 的情况：

```markdown
## Week 14 AI 使用记录

### 使用的工具
- Claude Code / Cursor / Copilot（选择你使用的）

### AI 生成占比
约 X%

### Prompt 示例
```
[你让 AI 生成的 Dockerfile/配置的 Prompt]
```

### 人工审查发现的问题
1. ...
2. ...
3. ...

### 人工修改的部分
1. ...
2. ...

### 学到的经验
- AI 擅长：...
- AI 不擅长：...
- 我的改进：...
```

---

## 提交物清单

### 必交文件
- [ ] `CHANGELOG.md`：版本变更日志
- [ ] `pom.xml`：包含 maven-shade-plugin 配置
- [ ] `config-dev.properties` 和 `config-prod.properties`
- [ ] `Config.java`：配置管理类
- [ ] `DEPLOYMENT.md`：部署说明文档
- [ ] `AI_LOG.md`：AI 使用日志
- [ ] Git 标签截图
- [ ] JAR 文件截图（或 MANIFEST.txt）
- [ ] 部署验证截图（3 张：功能、Network、控制台）

### 进阶作业文件（如完成）
- [ ] `Dockerfile`：容器化配置
- [ ] `.github/workflows/build.yml`：CI/CD 配置

### 目录结构参考
```
week_14_submission/
├── CHANGELOG.md
├── DEPLOYMENT.md
├── AI_LOG.md
├── git_tag_screenshot.png
├── jar_file_screenshot.png
├── deployment_screenshots/
│   ├── functionality.png
│   ├── network_tab.png
│   └── console.png
├── src/
│   └── main/
│       ├── java/com/campusflow/
│       │   ├── App.java
│       │   └── config/
│       │       └── Config.java
│       └── resources/
│           ├── config-dev.properties
│           └── config-prod.properties
├── pom.xml
└── Dockerfile（如完成进阶 1）
```

---

## 作业截止时间

- **基础作业**：本周日 23:59
- **进阶作业**：下周三 23:59

---

## 常见问题

### Q1: Maven 打包时报错 "Signature already exists"

这是因为不同的依赖库有数字签名文件，打包时会冲突。

**解决方法**：在 maven-shade-plugin 中添加过滤器排除签名文件（见任务 2 的配置示例）。

### Q2: 运行 JAR 时报错 "no main manifest attribute"

主类没有正确配置。

**解决方法**：
1. 检查 pom.xml 中的 `mainClass` 是否正确（`com.campusflow.Main`）
2. 重新打包：`mvn clean package`
3. 验证 MANIFEST.MF：`unzip -p target/*.jar META-INF/MANIFEST.MF | grep Main-Class`

### Q3: 部署到云平台后 API 返回 500

通常是数据库路径问题。

**解决方法**：
1. 检查 config-prod.properties 中的 `db.path`
2. 云平台的文件系统可能是只读的，需要使用持久化存储
3. 某些平台（如 Railway）会自动提供 `/data` 目录用于持久化

### Q4: 云平台分配的端口与应用监听的端口不一致

云平台会通过环境变量 `PORT` 告诉应用应该监听哪个端口。

**解决方法**：在 Config.java 中支持 `` `${PORT:8080}` `` 格式（见任务 3 的代码示例）。

### Q5: Railway 部署后自动休眠

Railway 的免费层有休眠机制（15 分钟无请求自动休眠）。

**解决方法**：
- 这是正常行为，首次访问会稍慢（冷启动）
- 生产环境建议升级付费计划

---

## 参考资源

- 如果你遇到困难，可以参考 `starter_code/` 中的示例代码（本作业暂不提供完整参考实现，请按文档步骤操作）
- Maven Shade Plugin 文档：https://maven.apache.org/plugins/maven-shade-plugin/
- Railway 部署指南：https://docs.railway.app/
- Fly.io 部署指南：https://fly.io/docs/
- Semantic Versioning 规范：https://semver.org/

---

## 学习建议

1. **版本管理是承诺**：版本号不是随便标，而是给用户的承诺。仔细思考 CampusFlow 是否准备好发布 v1.0.0。

2. **打包是用户体验的第一步**：如果用户连运行都不会，你的功能再强也没用。花时间把 JAR 做好。

3. **环境配置决定成败**：70% 的线上事故源于环境差异。认真处理配置管理。

4. **部署是里程碑**：成功部署后，CampusFlow 就是一个"真实可用"的产品了。享受这个时刻！

祝作业顺利！记住老潘的话："代码写完了只是 50%。"
