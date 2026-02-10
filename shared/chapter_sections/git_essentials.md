<!-- 每周 CHAPTER.md 中可引用此段。Agent 展开时直接复制。 -->

## Git 本周要点

本周 Git 学习要点：

```bash
# 基础操作
git add .
git commit -m "描述变更"
git push

# 分支操作
git checkout -b feature/xxx
git merge main

# 查看状态
git status
git log --oneline
```

**提交规范**：
- 使用动词开头（Add/Fix/Update/Refactor）
- 一句话描述变更内容
- 关联 Issue（如有）

**代码审查清单**：
- [ ] 代码能编译通过（`mvn compile`）
- [ ] 测试能通过（`mvn test`）
- [ ] 符合代码风格规范
