# Week 15 Starter Code 说明

<!--
  Week 15: 项目集市与展示准备
  本周不涉及新的 Java 代码，重点是展示准备
-->

## 本周重点

Week 15 不涉及新的 Java 代码开发。本周的重点是：

1. **设计演示脚本**：规划 10 分钟的叙事结构
2. **准备展示材料**：PPT、海报、二维码
3. **演练问答**：准备常见问题的答案
4. **跨组展示**：收集反馈并改进

## Starter Code 状态

Week 15 的 starter_code 与 Week 14 相同，因为：

- 本周是展示准备周，不涉及新功能开发
- CampusFlow 已经完成了核心功能（Week 09-14）
- 本周重点是"让别人看见你的作品"，而不是"写更多代码"

## 如何使用 Starter Code

### 1. 复制 Week 14 的代码

```bash
# 如果需要从 Week 14 复制代码
cp -r ../week_14/starter_code/* .
```

### 2. 运行 CampusFlow（用于演示）

```bash
# 编译
mvn clean package

# 运行
java -jar target/campusflow-1.0.0.jar
```

### 3. 准备演示数据

创建 5-10 个测试任务，用于展示：

```bash
# 启动应用后，使用 API 创建任务
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"title":"完成展示脚本","description":"设计10分钟的演示脚本","dueDate":"2026-02-20","completed":false}'
```

## 演示环境准备

### 本地演示（备用方案）

1. 启动 CampusFlow
2. 确保有测试数据（5-10 个任务）
3. 打开浏览器访问 http://localhost:8080
4. 准备好演示流程（创建 → 标记完成 → 过滤 → 删除）

### 在线演示（主要方案）

如果已经部署到 Railway：

1. 确认在线地址可访问（如 campusflow.up.railway.app）
2. 确认有测试数据
3. 准备好演示流程

### 备用方案：录屏

1. 使用 OBS 或录屏软件录制 CampusFlow 演示
2. 保存为 MP4 文件
3. 如果现场演示失败，播放录屏

## 展示材料

### PPT

参考 `examples/15_presentation_script.md` 中的演示脚本，设计 PPT：

- 封面：CampusFlow 标题 + 团队成员
- 目录：问题背景、技术选型、功能演示、工程实践、踩坑与成长
- 技术选型：Java + Javalin + SQLite
- 架构设计：领域模型 → Repository → Controller → API
- 功能演示：截图或录屏
- 工程实践：测试覆盖率、CI/CD
- 踩坑与成长：Bug Bash、AI 协作
- 项目链接：GitHub、在线演示

### 海报

参考 `examples/15_showcase_poster.html`，设计 A1 尺寸海报：

- 标题区：CampusFlow + 一句话介绍
- 核心内容区：问题 → 解决方案
- 技术栈和亮点
- 二维码：GitHub、在线演示、API 文档

### 二维码

使用以下工具生成二维码：

- GitHub：https://github.com/your-team/campusflow
- 在线演示：https://campusflow.up.railway.app
- API 文档：https://campusflow.up.railway.app/swagger-ui

工具：
- https://www.qrcode-generator.com/
- https://api.qrserver.com/v1/create-qr-code/

## 演示脚本

参考 `examples/15_presentation_script.md` 中的完整脚本：

### 开场（1 分钟）

> 大家好，我们是 CampusFlow 团队。CampusFlow 是一个"极简任务管理工具"——不是功能最全的，但代码最简洁、最容易理解。

### 主体（7 分钟）

1. 问题背景（1 分钟）：现有工具太复杂
2. 技术亮点（2 分钟）：Java + Javalin + SQLite
3. 功能演示（2 分钟）：创建、标记、过滤、删除
4. 工程实践（1 分钟）：测试、文档、部署
5. 踩坑与成长（1 分钟）：Bug Bash、AI 协作

### 结尾（2 分钟）

> CampusFlow 让我们学到了完整的软件工程流程。代码写得再好，如果没人能用，就没有价值。

## 问答准备

准备 10-15 个常见问题的答案：

1. 为什么用 Java？不用 Python 或 Go？
2. 为什么不用 Spring？
3. 为什么用 SQLite？不用 MySQL？
4. 你们的架构有什么特色？
5. CampusFlow 和 Notion 比有什么优势？
6. 下一步有什么计划？

参考 `examples/15_presentation_script.md` 中的标准答案。

## 反馈收集

使用 `examples/15_feedback_form.md` 收集反馈：

### 演示质量

- 叙事清晰度
- 时间控制
- 表达流畅度
- 眼神交流

### 内容质量

- 问题背景明确度
- 技术亮点突出度
- 功能演示完整性
- 踩坑与成长分享

### 材料质量

- PPT 清晰度
- 海报吸引力
- 演示环境稳定性
- 二维码可访问性

## 项目集市检查清单

### 展示前

- [ ] 演示脚本完成（10 分钟）
- [ ] PPT 完成（12-15 页）
- [ ] 海报完成（A1 尺寸）
- [ ] 二维码生成（GitHub、在线演示）
- [ ] 演示环境测试（在线 + 本地备份）
- [ ] 测试数据准备（5-10 个任务）
- [ ] 备用方案准备（录屏）
- [ ] 练习 5-10 遍

### 展示当日

- [ ] 提前 30 分钟到场
- [ ] 测试投影仪和声音
- [ ] 测试演示环境
- [ ] 准备好备份方案
- [ ] 深呼吸，放松

### 展示后

- [ ] 收集反馈
- [ ] 记录问答
- [ ] 复盘改进
- [ ] 感谢团队

## 下周预告

Week 16 将学习**工程复盘与总结**：

- 怎么总结一学期的经验
- 怎么为未来规划
- 怎么写复盘报告

CampusFlow 的旅程即将结束，但展示不是终点，而是起点——通过展示，你能获得反馈，继续改进。
