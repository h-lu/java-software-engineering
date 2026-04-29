# Week 14 Starter Code：发布与部署

这个目录提供一个可编译的 Week 14 起步骨架，不是完整答案。你需要把它接入自己的 CampusFlow 项目，完成版本管理、可执行 JAR、环境配置和部署说明。

## 运行命令

需要 Java 21 和 Maven 3.9+。

```bash
cd chapters/week_14/starter_code
mvn test
mvn package
java -cp target/classes com.campusflow.Main
```

完成 `maven-shade-plugin` TODO 后，应该可以改用：

```bash
java -jar target/campusflow-1.0.0.jar
```

## 需要编辑的文件

- `pom.xml`：TODO 添加 `maven-shade-plugin`，生成可执行 JAR。
- `src/main/java/com/campusflow/util/Version.java`：TODO 实现 SemVer 解析、比较和升级方法。
- `src/main/java/com/campusflow/config/Config.java`：TODO 支持 `CAMPUSFLOW_ENV`、`${PORT:8080}` 占位符和环境变量覆盖。
- `src/main/resources/config-*.properties`：TODO 按你的部署平台调整端口、数据库路径和日志级别。
- `CHANGELOG.md`：待办，改成你的真实版本变更记录。
- `DEPLOYMENT.md`：待办，记录真实云平台、环境变量、访问地址和验证截图。

如果保留作业中的统一 JAR 名称，请在 `pom.xml` 中把版本更新为 `1.0.0`，并配置 `<finalName>campusflow-1.0.0</finalName>` 或等价的 shade 输出。

## 本起步包不会替你完成

- 不会创建 Git tag。
- 不会生成生产可用的部署配置。
- 不会替你判断是否应该发布 `v1.0.0`。
- 不会保存任何真实密钥。敏感配置必须通过环境变量注入。
