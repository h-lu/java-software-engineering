# Semantic Versioning（语义化版本）规范

> 本示例介绍 Semantic Versioning 2.0.0 规范，用于统一软件版本号管理。

## 概述

**Semantic Versioning（SemVer）** 是一种版本号命名规范，用三段数字（MAJOR.MINOR.PATCH）传递变更信息：
- **MAJOR（主版本）**：不兼容的 API 变更
- **MINOR（次版本）**：向后兼容的功能新增
- **PATCH（补丁版本）**：向后兼容的问题修复

## 版本号格式

```
MAJOR.MINOR.PATCH[-PRERELEASE][+BUILD]

示例：
1.0.0          正式版本
1.0.1          补丁版本
1.1.0          次版本
2.0.0          主版本
1.0.0-beta.1   预发布版本
1.0.0+20130313144700 构建元数据
```

## 版本号决策树

```
变更类型判断：

1. 是否修复了 bug？
   ├─ 是 → PATCH 版本（1.0.0 → 1.0.1）
   └─ 否 → 继续

2. 是否新增了功能？
   ├─ 是 → 是否破坏了现有 API？
   │   ├─ 是 → MAJOR 版本（1.0.0 → 2.0.0）
   │   └─ 否 → MINOR 版本（1.0.0 → 1.1.0）
   └─ 否 → 继续

3. 是否重构了代码（保持 API 不变）？
   ├─ 是 → PATCH 版本（1.0.0 → 1.0.1）
   └─ 否 → 重新评估变更类型
```

## CampusFlow 版本历史示例

```
v1.0.0 (2026-01-15) - 初始发布
- 功能：创建任务、编辑任务、标记完成
- API：/api/tasks (GET, POST, PUT, DELETE)

v1.0.1 (2026-01-20) - PATCH 版本
- 修复：任务标题超过 100 字符时不再截断
- 修复：删除任务后不再返回空指针异常
- 风险：低，可直接升级

v1.1.0 (2026-02-01) - MINOR 版本
- 新增：任务搜索功能（按关键词）
- 新增：任务过滤（按状态、按日期）
- API 新增：GET /api/tasks/search?q=keyword
- 风险：中，可直接升级

v2.0.0 (2026-03-01) - MAJOR 版本
- 破坏性变更：API 端点从 /api/tasks 改为 /api/v2/tasks
- 新增：用户认证（需要 Bearer Token）
- 删除：旧的 XML 格式响应（只保留 JSON）
- 风险：高，需要修改前端代码
```

## Git Tag 使用示例

```bash
# 查看所有标签
git tag

# 创建附注标签（推荐）
git tag -a v1.0.0 -m "Release v1.0.0: Initial release

Features:
- Task management (create, edit, delete)
- Mark tasks as completed
- RESTful API with OpenAPI documentation

Deployment:
- Packaged as executable JAR
- Deployed to Railway"

# 推送标签到远程仓库
git push origin v1.0.0
git push origin --tags  # 推送所有标签

# 查看标签信息
git show v1.0.0

# 检出某个版本（查看该版本的代码）
git checkout v1.0.0

# 删除标签
git tag -d v1.0.0          # 删除本地标签
git push origin --delete v1.0.0  # 删除远程标签

# 给过去的 commit 打标签
git tag -a v0.9.0 <commit-hash> -m "Beta release"
```

## Commit Message 规范

```bash
# 推荐格式：<type>(<scope>): <subject>

feat: add task search feature        # 新功能
fix: resolve task title truncation    # Bug 修复
docs: update API documentation        # 文档变更
refactor: simplify config management  # 重构
test: add integration tests           # 测试
chore: upgrade Maven to 3.9.5         # 构建/工具变更

# 发布版本
release: v1.0.0
```

## 预发布版本

```
v1.0.0-alpha    内部测试版
v1.0.0-alpha.1  内部测试版（第1次）
v1.0.0-beta     公开测试版
v1.0.0-beta.1   公开测试版（第1次）
v1.0.0-rc.1     候选发布版（Release Candidate）
```

预发布版本的优先级：
```
alpha < beta < rc < 正式版本
```

## 版本号比较

```
1.0.0 < 2.0.0   (MAJOR 优先)
1.0.0 < 1.1.0   (MINOR 其次)
1.0.0 < 1.0.1   (PATCH 最后)

1.0.0-alpha < 1.0.0-beta.1 < 1.0.0-beta.2 < 1.0.0-rc.1 < 1.0.0
```

## 参考资料

- [Semantic Versioning 2.0.0 官方规范](https://semver.org/)
- [Keep a Changelog](https://keepachangelog.com/) - 变更日志格式建议

## 常见问题

### Q: 什么时候该升级 MAJOR 版本？

**A**: 当你的变更会破坏现有代码时，例如：
- 删除或重命名了 API 端点
- 修改了方法签名
- 改变了返回值的数据结构

### Q: 修 bug 必须用 PATCH 版本吗？

**A**: 如果修复不改变 API 行为，用 PATCH。如果修复改变了 API 行为（即使是为了修复 bug），应该考虑 MINOR 或 MAJOR。

### Q: 预发布版本怎么用？

**A**: 在正式发布前，先发布 alpha/beta/rc 版本收集反馈。例如：
- v1.0.0-alpha: 内部团队测试
- v1.0.0-beta: 公开测试，收集用户反馈
- v1.0.0-rc.1: 候选版本，主要修 bug，不加新功能
- v1.0.0: 正式发布

### Q: 构建元数据（+BUILD）怎么用？

**A**: 构建元数据不影响版本优先级，主要用于记录构建信息：
```
1.0.0+20130313144700
1.0.0+exp.sha.5114f85
```
