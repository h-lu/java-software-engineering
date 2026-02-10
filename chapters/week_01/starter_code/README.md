# 项目起步模板

这个目录包含 Week 01 作业的起步代码。

## 使用说明

### 1. 创建 Maven 项目

```bash
mvn archetype:generate \
    -DgroupId=com.campusflow \
    -DartifactId=week01-hello \
    -DarchetypeArtifactId=maven-archetype-quickstart \
    -DarchetypeVersion=1.4 \
    -DinteractiveMode=false
```

### 2. 修改 pom.xml

更新 Java 版本为 17：

```xml
<properties>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
</properties>
```

### 3. 复制起步代码

将本目录下的 .java 文件复制到 `src/main/java/com/campusflow/` 目录。

### 4. 实现功能

按照代码中的 TODO 注释完成任务。

### 5. 运行测试

```bash
mvn compile
mvn exec:java -Dexec.mainClass="com.campusflow.HelloWorld"
mvn exec:java -Dexec.mainClass="com.campusflow.BusinessCard"
```

## 文件说明

| 文件 | 说明 |
|------|------|
| HelloWorld.java | 第一个程序，熟悉基本结构 |
| BusinessCard.java | 名片生成器，练习输入输出和验证 |
