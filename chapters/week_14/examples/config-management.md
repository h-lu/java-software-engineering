# 环境配置管理

> 本示例介绍三种环境配置方案，处理开发/测试/生产环境的差异。

## 方案对比

| 方案 | 优点 | 缺点 | 适用场景 |
|------|------|------|----------|
| **配置文件 + 环境变量** | 简单、直观、易于理解 | 配置文件可能泄露敏感信息 | 小型项目、学生项目 |
| **12-Factor App（纯环境变量）** | 配置与代码完全分离、云平台友好 | 命令行过长、不易管理 | 容器化部署、云平台 |
| **配置中心** | 统一管理、版本控制、动态更新 | 复杂、需要额外服务 | 大型分布式系统 |

---

## 方案 1：配置文件 + 环境变量

### Config 类实现

```java
package com.campusflow.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 配置管理类
 * 从环境变量读取环境名称（dev/test/prod），加载对应配置文件
 */
public class Config {
    private final Properties props;
    private final String env;

    public Config() {
        // 从环境变量读取环境名称（dev/test/prod）
        this.env = System.getenv("CAMPUSFLOW_ENV") != null
            ? System.getenv("CAMPUSFLOW_ENV")
            : "dev";  // 默认开发环境

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
        return props.getProperty("db.path", "campusflow.db");
    }

    public int getPort() {
        return Integer.parseInt(props.getProperty("server.port", "8080"));
    }

    public String getLogLevel() {
        return props.getProperty("log.level", "INFO");
    }

    public String getEnv() {
        return env;
    }
}
```

### 配置文件示例

**config-dev.properties**（开发环境）：
```properties
# 数据库配置
db.path=campusflow.db

# 服务器配置
server.port=8080

# 日志配置
log.level=DEBUG

# API 配置
api.base_path=/api
```

**config-prod.properties**（生产环境）：
```properties
# 数据库配置（持久化存储）
db.path=/var/data/campusflow.db

# 服务器配置（云平台会自动设置 PORT）
server.port=${PORT:80}

# 日志配置（只记录警告和错误）
log.level=WARN

# API 配置
api.base_path=/api
```

### 启动命令

```bash
# 开发环境
CAMPUSFLOW_ENV=dev java -jar campusflow-1.0.0.jar

# 生产环境
CAMPUSFLOW_ENV=prod java -jar campusflow-1.0.0.jar
```

---

## 方案 2：12-Factor App 方法论（纯环境变量）

### Config 类实现

```java
package com.campusflow.config;

/**
 * 12-Factor App 风格配置
 * 所有配置通过环境变量注入，不使用配置文件
 */
public class Config {
    public String getDbPath() {
        // 优先读取环境变量，没有则用默认值
        return System.getenv().getOrDefault("DB_PATH", "campusflow.db");
    }

    public int getPort() {
        return Integer.parseInt(
            System.getenv().getOrDefault("SERVER_PORT", "8080")
        );
    }

    public String getLogLevel() {
        return System.getenv().getOrDefault("LOG_LEVEL", "INFO");
    }

    public String getEnv() {
        return System.getenv().getOrDefault("APP_ENV", "dev");
    }
}
```

### 启动命令

```bash
# 开发环境
DB_PATH=campusflow.db \
SERVER_PORT=8080 \
LOG_LEVEL=DEBUG \
java -jar campusflow-1.0.0.jar

# 生产环境
DB_PATH=/var/data/campusflow.db \
SERVER_PORT=80 \
LOG_LEVEL=WARN \
java -jar campusflow-1.0.0.jar
```

### 优点

- 配置与代码完全分离
- 云平台友好（Railway、Render 等自动设置环境变量）
- 无需管理配置文件

---

## 方案 3：配置中心（大型项目）

对于大型分布式系统，可以使用专门的配置中心：

- **Spring Cloud Config**：Spring 生态的配置中心
- **Apollo**：携程开源的配置中心
- **Nacos**：阿里云的配置中心
- **Consul**：HashiCorp 的服务发现和配置中心

这些工具提供：
- 配置的统一管理
- 配置的版本控制
- 配置的动态更新（无需重启服务）

---

## .gitignore 配置

### 忽略敏感配置文件

```gitignore
# 配置文件（包含敏感信息）
config-prod.properties
config-*.local.properties

# 敏感信息文件
.env
.env.local
.env.production

# 数据库文件
*.db
*.db-journal

# 日志文件
logs/
*.log
```

### 配置文件模板

**config-prod.properties.template**（模板文件）：
```properties
# 数据库配置
db.path=/var/data/campusflow.db

# 服务器配置
server.port=${PORT:80}

# 日志配置
log.level=WARN

# 敏感配置（请替换为实际值）
database.password=${DB_PASSWORD}
api.key=${API_KEY}
```

---

## 最佳实践

### 1. 敏感信息不要写进配置文件

```properties
# ❌ 错误：密码硬编码
db.password=MySecretPassword123

# ✅ 正确：使用环境变量占位符
db.password=${DB_PASSWORD}
```

### 2. 生产环境配置要加密存储

```bash
# 使用密钥管理服务（如 AWS Secrets Manager、HashiCorp Vault）
export DB_PASSWORD=$(aws secretsmanager get-secret-value --secret-id campusflow-db --query SecretString --output text)
```

### 3. 配置文件不要提交到 Git

```gitignore
# .gitignore
config-prod.properties
.env
*.local.properties
```

### 4. 提供配置文件模板

```bash
# 项目根目录
cp config-prod.properties.template config-prod.properties
# 编辑 config-prod.properties，填入实际配置
```

---

## 常见问题

### Q: 如何在本地测试生产配置？

**A**: 创建 `config-local-prod.properties`（不提交到 Git）：
```bash
CAMPUSFLOW_ENV=local-prod java -jar campusflow-1.0.0.jar
```

### Q: 云平台（Railway/Render）如何设置环境变量？

**A**: 在平台控制台中设置：
- Railway：Settings → Variables
- Render：Environment → Environment Variables

### Q: Docker 容器如何注入环境变量？

**A**:
```dockerfile
# Dockerfile
ENV DB_PATH=/var/data/campusflow.db
ENV SERVER_PORT=8080
```

或运行时注入：
```bash
docker run -e DB_PATH=/data/campusflow.db -e SERVER_PORT=80 campusflow:1.0.0
```
