# 示例：小北和阿码的 main 分支冲突问题

本示例演示直接在 main 分支开发导致的代码覆盖问题。

## 场景描述

小北和阿码都在 main 分支上开发，结果小北的代码被阿码的提交覆盖了。

---

## 时间线

### 周五下午 3:00 - 小北提交筛选功能

```bash
# 小北的操作
$ git add .
$ git commit -m "feat: 添加任务筛选功能"
$ git push origin main
```

**推送成功**，小北下班了。

---

### 周五下午 5:00 - 阿码提交排序功能

```bash
# 阿码的操作（没有先 pull）
$ git add .
$ git commit -m "feat: 添加任务排序功能"
$ git push origin main
```

**推送失败**，Git 提示：

```
! [rejected]        main -> main (fetch first)
error: failed to push some refs to 'origin'
hint: Updates were rejected because the remote contains work that you do
hint: not have locally.
```

---

### 阿码执行 git pull 后重新推送

```bash
$ git pull origin main
$ git push origin main
```

这次推送成功。

---

## 问题：小北的代码消失了

周一早上，小北发现 `TaskManager.java` 里自己的筛选功能代码不见了：

```java
// TaskManager.java - 周一早上的状态
public class TaskManager {
    private List<Task> tasks = new ArrayList<>();

    // ❌ 小北的筛选功能不见了！

    // ✅ 只有阿码的排序功能
    public List<Task> sortByDeadline() {
        // ...
    }
}
```

---

## 原因分析

Git 历史被覆盖：

```
main 分支历史：

A --- B(小北: 筛选功能) --- C(阿码: 排序功能, 覆盖 B)
         ^
         |
    小北的提交被合并冲突覆盖
```

阿码的 `git pull` 产生了合并冲突，Git 自动合并时保留了阿码的版本，导致小北的改动丢失。

---

## 教训

**不要在 main 分支直接开发！**

正确的做法是使用 Feature Branch 工作流（见 02_feature_branch.sh）。
