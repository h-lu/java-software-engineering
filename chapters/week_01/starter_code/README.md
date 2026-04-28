# Week 01 Starter Code：Java 基础与名片生成器

这是一个独立 Maven 起步项目，用来完成 Week 01 的个人名片生成器练习。代码只提供可运行骨架和 TODO，不包含完整答案。

## 目录结构

```text
starter_code/
├── pom.xml
├── README.md
├── src/main/java/com/week01/
│   ├── HelloWorld.java
│   ├── VariableDemo.java
│   ├── ScannerDemo.java
│   └── BusinessCard.java
└── src/test/java/com/week01/
    └── StarterSmokeTest.java
```

## 运行命令

```bash
cd chapters/week_01/starter_code
mvn test
java -cp target/classes com.week01.HelloWorld
java -cp target/classes com.week01.BusinessCard
```

## 你需要编辑的文件

- `HelloWorld.java`：补全硬编码名片输出。
- `VariableDemo.java`：把姓名、职位、邮箱、电话改成变量并拼接输出。
- `ScannerDemo.java`：使用 `Scanner` 读取姓名、职位和年龄。
- `BusinessCard.java`：完成主练习，读取姓名、职位、邮箱、年龄、工作年限并格式化输出。

## TODO 清单

- TODO 1：保证类名和文件名一致。
- TODO 2：用 `String`、`int`、`double` 存储不同类型的信息。
- TODO 3：数字输入先用 `scanner.nextLine()` 读取，再用 `Integer.parseInt` / `Double.parseDouble` 转换。
- TODO 4：不要在 Week 01 主线里写复杂异常处理；挑战题可用循环做简单验证。
- TODO 5：把最终代码提交到你自己的团队仓库。

`mvn test` 只是烟雾测试，确认骨架能编译运行。它不会替你验证作业是否完成，最终验收以 `ASSIGNMENT.md` 为准。
