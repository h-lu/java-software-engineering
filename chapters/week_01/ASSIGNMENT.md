# Week 01 作业：环境配置与第一个 Java 程序

## 作业目标

1. 完成 Java 开发环境配置（JDK 17、IntelliJ IDEA、Maven）
2. 编写并运行你的第一个 Java 程序
3. 熟悉 Java 的基本语法（变量、输入输出）
4. 组建团队并建立 Git 仓库

---

## 作业清单

### 任务 1：开发环境配置（20%）

**要求**：
- [ ] 安装 JDK 17 并验证 (`java -version` 显示 17.x)
- [ ] 安装 IntelliJ IDEA（社区版即可）
- [ ] 安装 Maven 并验证 (`mvn -version`)
- [ ] 配置 Git 用户信息

**提交物**：
- 截图：终端执行 `java -version` 和 `mvn -version` 的结果
- 截图：IntelliJ IDEA 启动界面

---

### 任务 2：第一个 Java 程序（30%）

**要求**：
创建一个 `HelloWorld.java`，包含：
- 输出 "Hello, World!"
- 声明至少 3 个不同类型的变量（String、int、double）
- 使用 `System.out.println` 输出这些变量的值

**进阶要求（可选加分）**：
- 使用 `Scanner` 获取用户输入
- 根据输入生成个性化问候语

**提交物**：
- `HelloWorld.java` 源代码
- 运行截图

---

### 任务 3：个人信息卡片程序（30%）

**要求**：
编写一个交互式程序 `ProfileCard.java`：

1. 使用 `Scanner` 获取以下信息：
   - 姓名（String）
   - 年龄（int）
   - 专业（String）
   - 邮箱（String）

2. 格式化输出一张个人信息卡片，例如：
```
================================
         个人信息卡片
================================
姓名：李小明
年龄：20 岁
专业：计算机科学与技术
邮箱：xiaoming@example.com
================================
```

**注意事项**：
- 处理 `nextInt()` 后的换行符问题
- 程序结束时关闭 Scanner

**提交物**：
- `ProfileCard.java` 源代码
- 运行截图（展示输入和输出）

---

### 任务 4：团队组建与项目启动（20%）

**要求**：
1. 组建 2-3 人团队
2. 确定团队名称
3. 从 CampusFlow 选题池中选择项目方向
4. 创建 GitHub 仓库并提交初始代码
5. 确定第一轮首席架构师（Week 02-03）

**提交物**：
- 团队成员名单
- 项目名称和简要描述
- GitHub 仓库链接
- 第一轮首席架构师姓名

---

## 提交要求

1. **截止时间**：本周日 23:59
2. **提交方式**：
   - 个人作业：通过课程管理系统提交
   - 团队信息：在团队仓库的 README.md 中记录
3. **格式**：
   - 源代码文件（.java）
   - 运行截图（.png 或 .jpg）
   - 团队信息文档

---

## 评分标准

| 任务 | 分值 | 评分要点 |
|------|------|----------|
| 环境配置 | 20% | 环境正确安装，版本符合要求 |
| Hello World | 30% | 代码正确，能编译运行 |
| 个人信息卡片 | 30% | 功能完整，交互正确，格式美观 |
| 团队组建 | 20% | 团队组建完成，仓库建立 |

---

## 常见问题

**Q：JDK 和 JRE 有什么区别？**  
A：JDK（Java Development Kit）包含 JRE（Java Runtime Environment）以及编译器、调试器等开发工具。开发 Java 程序需要 JDK，运行 Java 程序只需要 JRE。

**Q：IntelliJ IDEA 社区版够用吗？**  
A：对于学习来说完全够用。旗舰版（Ultimate）主要是增加了 Web/企业级开发支持。

**Q：为什么 `nextInt()` 后要用 `nextLine()`？**  
A：`nextInt()` 只读取数字，不会读取行尾的回车符。如果不处理，后续的 `nextLine()` 会读到这个回车，导致看起来像是跳过了输入。

**Q：Chief Architect 要做什么？**  
A：第一轮 Chief Architect 负责 Week 02-03 的设计决策，包括主持团队讨论、撰写第一份 ADR（架构决策记录）。详细要求见 `shared/book_project.md`。

---

## 参考资料

- [JDK 17 下载](https://www.oracle.com/java/technologies/downloads/#java17)
- [IntelliJ IDEA 下载](https://www.jetbrains.com/idea/download/)
- [Git 基础教程](https://www.liaoxuefeng.com/wiki/896043488029600)
- `shared/book_project.md` - CampusFlow 项目设计
