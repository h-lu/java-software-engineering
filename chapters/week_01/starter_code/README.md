# Week 01 Starter Code

这是 Week 01 的起步代码，帮助你快速上手 Java 开发。

## 目录结构

```
starter_code/
├── pom.xml                                    # Maven 项目配置
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── week01/
│   │               ├── HelloWorld.java        # 练习 2：Hello World
│   │               ├── VariableDemo.java      # 练习 3：变量声明
│   │               ├── ScannerDemo.java       # 练习 4：Scanner 输入
│   │               └── BusinessCard.java      # 练习 5：名片生成器
│   └── test/
│       └── java/
│           └── com/
│               └── week01/
│                   └── BusinessCardTest.java  # 单元测试（可选）
```

## 如何使用

### 方法 1：使用 Maven 命令行

```bash
# 编译项目
cd /path/to/week01/starter_code
mvn compile

# 运行 HelloWorld
mvn exec:java -Dexec.mainClass="com.week01.HelloWorld"

# 运行 BusinessCard
mvn exec:java -Dexec.mainClass="com.week01.BusinessCard"

# 运行测试
mvn test
```

### 方法 2：使用 IntelliJ IDEA

1. 打开 IntelliJ IDEA
2. 选择 `File` → `Open` → 选择 `starter_code` 目录
3. 等待 Maven 导入完成
4. 在左侧项目树中找到 `src/main/java/com/week01/`
5. 右键点击要运行的 Java 文件 → `Run 'xxx.main()'`

## 练习说明

### 练习 1：环境配置验证

按照作业文档中的步骤，验证 JDK、Maven、Git 是否正确安装。

### 练习 2：Hello World 程序

打开 `HelloWorld.java`，查看代码并运行。理解 Java 的基本结构。

### 练习 3：变量声明练习

打开 `VariableDemo.java`，尝试修改代码，理解静态类型的变量声明。

### 练习 4：Scanner 输入练习

打开 `ScannerDemo.java`，运行并输入信息。理解如何读取用户输入。

### 练习 5：名片生成器

打开 `BusinessCard.java`，这是你的主要任务。根据作业要求完成它。

**起步提示**：
- 代码已经包含了基本框架
- 你需要补全读取输入的逻辑
- 注意处理换行符问题（使用 `nextLine()` + `parseInt()`）

## 常见问题

### Q: 运行时提示 "找不到或无法加载主类"

**A**: 确保在 `starter_code` 目录下运行命令，并且已经执行过 `mvn compile`。

### Q: IntelliJ 中无法识别 JDK 17

**A**: 在 `File` → `Project Structure` → `Project` 中设置 Project SDK 为 JDK 17。

### Q: Maven 下载依赖很慢

**A**: 可以配置国内镜像源（如阿里云）来加速下载。

## 下一步

完成所有练习后：
1. 提交代码到你的团队 Git 仓库
2. 编写作业报告
3. 确保团队项目启动完成（Git 仓库、README、项目锚点）

祝你学习愉快！
