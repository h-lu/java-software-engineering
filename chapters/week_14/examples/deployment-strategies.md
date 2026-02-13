# 部署策略

> 本示例介绍三种部署方案，从云平台一键部署到容器化部署。

## 方案对比

| 方案 | 复杂度 | 成本 | 适用场景 | 学习曲线 |
|------|--------|------|----------|----------|
| **云平台一键部署** | 低 | 免费/低 | 小型项目、学生项目 | 低 |
| **Docker 容器部署** | 中 | 中 | 中型项目、需要灵活性 | 中 |
| **传统 VPS 部署** | 高 | 中/高 | 大型项目、完全控制 | 高 |

---

## 方案 1：云平台一键部署（推荐新手）

### 平台选择

| 平台 | 免费额度 | Java 支持 | 推荐指数 |
|------|----------|-----------|----------|
| **Railway** (railway.app) | $5 免费额度/月 | 自动检测 Maven | ⭐⭐⭐⭐⭐ |
| **Render** (render.com) | 750 小时/月 | 支持 Java | ⭐⭐⭐⭐ |
| **Fly.io** (fly.io) | 3 个小型应用 | 支持 Docker/JAR | ⭐⭐⭐⭐ |

### Railway 部署步骤

#### 1. 连接 GitHub

```
登录 Railway → 点击 "New Project" → 选择 "Deploy from GitHub repo"
```

#### 2. 选择仓库

```
选择 campusflow 仓库 → Railway 自动检测 Maven 项目
```

#### 3. 配置环境变量

在 Railway 控制台设置（Settings → Variables）：

```
DB_PATH=/var/data/campusflow.db
SERVER_PORT=8080（Railway 会自动设置 PORT 环境变量）
CAMPUSFLOW_ENV=prod
```

#### 4. 自动部署

Railway 自动执行：
```
1. 检测 Maven 项目
2. 运行 mvn package
3. 启动 JAR（自动检测主类）
4. 分配公网 URL
5. 配置 HTTPS（自动证书）
```

#### 5. 获取访问地址

Railway 给你一个 URL：
```
https://campusflow.up.railway.app
```

### Render 部署步骤

#### 1. 连接 GitHub

```
登录 Render → 点击 "New +" → 选择 "Web Service"
```

#### 2. 选择仓库

```
选择 campusflow 仓库 → 配置构建命令
```

#### 3. 配置构建和启动

```
Build Command: mvn clean package -DskipTests
Start Command: java -jar target/campusflow-1.0.0.jar
```

#### 4. 配置环境变量

```
DB_PATH=/var/data/campusflow.db
PORT=8080
CAMPUSFLOW_ENV=prod
```

#### 5. 自动部署

Render 自动：
```
1. 运行构建命令
2. 启动服务
3. 分配公网 URL（*.onrender.com）
4. 配置 HTTPS（自动证书）
```

---

## 方案 2：Docker 容器部署（推荐进阶）

### Dockerfile 示例

```dockerfile
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
```

### .dockerignore（减小构建上下文）

```
# .dockerignore
target/
*.iml
.idea/
.DS_Store
.git/
*.md
.env
config-prod.properties
```

### 构建和运行

```bash
# 构建镜像
docker build -t campusflow:1.0.0 .

# 运行容器（本地测试）
docker run -d \
  --name campusflow \
  -p 8080:8080 \
  -e DB_PATH=/data/campusflow.db \
  -v campusflow-data:/data \
  campusflow:1.0.0

# 查看日志
docker logs -f campusflow

# 停止容器
docker stop campusflow

# 删除容器
docker rm campusflow
```

### 推送到容器注册表

```bash
# 登录 Docker Hub
docker login

# 打标签
docker tag campusflow:1.0.0 your-dockerhub/campusflow:1.0.0
docker tag campusflow:1.0.0 your-dockerhub/campusflow:latest

# 推送到 Docker Hub
docker push your-dockerhub/campusflow:1.0.0
docker push your-dockerhub/campusflow:latest
```

### GitHub Container Registry（推荐）

```bash
# 登录 GHCR
echo $GITHUB_TOKEN | docker login ghcr.io -u USERNAME --password-stdin

# 打标签
docker tag campusflow:1.0.0 ghcr.io/your-username/campusflow:1.0.0

# 推送到 GHCR
docker push ghcr.io/your-username/campusflow:1.0.0
```

### Fly.io 部署（使用 Docker）

```bash
# 安装 Fly CLI
macOS: brew install flyctl
Linux: curl -L https://fly.io/install.sh | sh

# 登录
flyctl auth login

# 初始化项目（自动生成 fly.toml）
flyctl launch

# 部署
flyctl deploy

# 查看日志
flyctl logs

# 获取访问地址
flyctl info
```

---

## 方案 3：传统 VPS 部署（不推荐新手）

### 服务器配置（AWS EC2 / 阿里云 ECS）

#### 1. 安装 Java

```bash
# Ubuntu/Debian
sudo apt update
sudo apt install -y openjdk-17-jre-headless

# CentOS/RHEL
sudo yum install -y java-17-openjdk-headless

# 验证安装
java -version
```

#### 2. 创建应用目录

```bash
sudo mkdir -p /opt/campusflow
sudo mkdir -p /var/data/campusflow
sudo chown -R $USER:$USER /opt/campusflow
```

#### 3. 上传 JAR

```bash
# 本地打包
mvn clean package

# 上传到服务器
scp target/campusflow-1.0.0.jar user@server:/opt/campusflow/
scp config-prod.properties user@server:/opt/campusflow/
```

#### 4. 创建 systemd 服务

```bash
# /etc/systemd/system/campusflow.service
[Unit]
Description=CampusFlow Application
After=network.target

[Service]
Type=simple
User=www-data
WorkingDirectory=/opt/campusflow
Environment="CAMPUSFLOW_ENV=prod"
Environment="DB_PATH=/var/data/campusflow.db"
ExecStart=/usr/bin/java -jar /opt/campusflow/campusflow-1.0.0.jar
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

#### 5. 启动服务

```bash
# 重载 systemd
sudo systemctl daemon-reload

# 启动服务
sudo systemctl start campusflow

# 开机自启
sudo systemctl enable campusflow

# 查看状态
sudo systemctl status campusflow

# 查看日志
sudo journalctl -u campusflow -f
```

#### 6. 配置 Nginx 反向代理

```nginx
# /etc/nginx/sites-available/campusflow
server {
    listen 80;
    server_name campusflow.example.com;

    location / {
        proxy_pass http://localhost:8080;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_cache_bypass $http_upgrade;
    }
}
```

```bash
# 启用配置
sudo ln -s /etc/nginx/sites-available/campusflow /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl reload nginx
```

#### 7. 配置 HTTPS（Let's Encrypt）

```bash
# 安装 Certbot
sudo apt install -y certbot python3-certbot-nginx

# 获取证书
sudo certbot --nginx -d campusflow.example.com

# 自动续期
sudo certbot renew --dry-run
```

---

## 部署检查清单

### 部署前

- [ ] 所有测试通过
- [ ] 配置文件正确（生产环境）
- [ ] 敏感信息通过环境变量注入
- [ ] 数据库路径可写
- [ ] 日志配置正确

### 部署后

- [ ] 服务正常启动
- [ ] 健康检查通过
- [ ] 公网可访问
- [ ] HTTPS 正常工作
- [ ] 日志正常输出

### 监控告警

- [ ] 服务状态监控
- [ ] 错误日志告警
- [ ] 资源使用监控
- [ ] 备份策略

---

## 常见问题

### Q: Railway/Render 免费额度有限制吗？

**A**:
- Railway：$5/月免费额度，用完需要付费
- Render：750 小时/月（约 31 天），但免费服务会休眠

### Q: 如何实现零停机部署？

**A**:
- 蓝绿部署：启动新版本，测试通过后切换流量
- 滚动更新：逐个替换实例
- Railway/Render 支持零停机部署

### Q: 数据库怎么办？

**A**:
- Railway/Render：支持持久化存储（需付费）
- Fly.io：支持 Volume 挂载
- VPS：本地 SQLite 或云数据库

### Q: 如何回滚？

**A**:
- Railway：自动保留历史版本，可一键回滚
- Render：手动回滚到 Git 旧版本
- Docker：重新部署旧版本镜像
