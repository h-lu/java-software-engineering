# Week 04：团队协作与 Code Review

> "代码是写给人看的，只是顺便让机器执行。"
> — Harold Abelson

2026 年的开发者生态正在经历一场静默的转变。Stack Overflow 的开发者调查显示，超过 70% 的专业开发者将大部分时间花在阅读和理解他人代码上，而非编写新代码。GitHub 的年度报告则给出了一个更具说服力的数字：采用系统化 Code Review 流程的团队，代码缺陷率比未采用团队低 40% 以上。

这背后的逻辑很简单——代码的生命周期中，"被阅读"的时间远超"被编写"的时间。你今天写的一行代码，可能在三个月后由另一位开发者在调试时读到，在一年后由你自己在排查 bug 时重新理解。

而在 AI 辅助编程迅速普及的 2026 年，这一趋势变得更加明显。AI 可以帮你快速生成代码，但审查 AI 代码、与团队协作、维护代码质量，这些软技能变得前所未有的重要。因为 AI 生成的代码同样需要被人类理解、审查和维护。

本周是"思维奠基"阶段的最后一周，我们将从个人编程转向团队协作。你会学习 Git 分支工作流、Pull Request 流程、Code Review 的实践技巧，以及如何撰写 ADR-002（数据存储方案决策）。这些不是"额外的流程"，而是规模化开发的必需品——当你的项目从"一个人维护"变成"三个人协作"，代码审查和团队沟通就成了质量的生命线。

---

## 前情提要

过去三周，你完成了 CampusFlow 的基础建设：Week 01 搭建了 Java 开发环境和 CLI 交互框架；Week 02 设计了核心领域模型（Task、TaskManager 等类），撰写了 ADR-001；Week 03 添加了异常处理和防御式编程，让程序能优雅应对各种非法输入。

但直到现在，你都是在"单人开发"模式下工作——自己写代码、自己测试、自己决定一切。现实世界中的软件工程是团队运动。本周，你将学习如何与队友协作开发：如何用 Git 分支隔离各自的改动，如何通过 Pull Request 提交代码，如何进行有效的 Code Review，以及当意见分歧时如何沟通解决。

---

## 学习目标

完成本周学习后，你将能够：
1. 使用 Git Feature Branch 工作流进行团队协作开发
2. 创建和审查 Pull Request，使用审查清单系统化评估代码质量
3. 给出建设性的 Code Review 反馈，处理技术分歧
4. 撰写 ADR-002（数据存储方案决策）
5. 在团队环境中应用已学的 SOLID 原则和异常处理策略

---

<!--
贯穿案例：CampusFlow 团队协作开发
- 第 1 节：展示"直接在 main 分支开发"的混乱场景（代码冲突、互相覆盖），引出分支工作流的必要性
- 第 2 节：学习 Feature Branch 工作流，为 CampusFlow 创建功能分支并提交改动
- 第 3 节：创建第一个 Pull Request，学习审查清单的使用
- 第 4 节：进行 Code Review 实践，发现代码中的问题并给出反馈
- 第 5 节：处理审查意见和团队分歧，学习建设性的沟通技巧
- 第 6 节：撰写 ADR-002，记录数据存储方案决策

最终成果：一个经过团队协作开发的 CampusFlow 版本，包含完整的 Git 提交历史、至少一次 Pull Request 和 Code Review 记录，以及 ADR-002 文档

认知负荷预算：
- 本周新概念（6 个，预算上限 6 个）：
  1. Feature Branch 工作流（feature branch workflow）
  2. Pull Request（PR）
  3. Code Review（代码审查）
  4. 审查清单（review checklist）
  5. 冲突解决（conflict resolution）
  6. 架构决策记录 ADR-002（数据存储方案）
- 结论：✅ 在预算内

回顾桥设计（至少 2 个）：
- [领域建模/类设计]（来自 week_02）：在第 3 节 Code Review 中，审查清单包含"是否符合单一职责原则"
- [异常处理/防御式编程]（来自 week_03）：在第 3 节 Code Review 中，审查清单包含"异常处理是否完善"
- [封装]（来自 week_02）：在第 4 节审查反馈中，检查 private 字段和 getter/setter 的使用

CampusFlow 本周推进：
- 上周状态：核心领域模型完成，异常处理添加完毕，程序能优雅处理非法输入
- 本周改进：团队组建完成，建立协作工作流，完成 ADR-002（数据存储方案决策）
- 涉及的本周概念：Feature Branch、Pull Request、Code Review、ADR-002
- 建议落盘位置：docs/adr/002-数据存储方案.md + Git 协作历史

角色出场规划：
- 小北：在第 1 节中直接在 main 分支提交代码，结果覆盖了队友的改动，引出分支工作流的必要性
- 阿码：在第 4 节 Code Review 中提出"为什么不用 AI 生成审查清单"，引出人工审查的价值
- 老潘：在第 5 节中分享"线上事故因为没做 Code Review"的真实故事，强调审查的工程价值

AI 小专栏 #1（放在第 2 节之后）：
- 主题：AI 辅助 Code Review 的边界
- 连接点：呼应第 2 节的 Pull Request 流程——AI 可以辅助审查，但无法替代人类对设计决策的判断
- 建议搜索词："AI code review tools 2026" "GitHub Copilot code review statistics 2025"

AI 小专栏 #2（放在第 4 节之后）：
- 主题：团队协作中的 AI 使用规范
- 连接点：呼应第 4 节的 Code Review 实践——团队需要建立 AI 使用的"公约"，明确哪些可以用 AI、哪些必须人工审查
- 建议搜索词："team AI coding policy 2026" "AI generated code review guidelines 2025"
-->

---

## 1. 当代码互相覆盖时——为什么需要分支

小北和队友阿码正在一起开发 CampusFlow。两人分工明确：小北负责"任务筛选功能"，阿码负责"任务排序功能"。他们都觉得自己负责的模块很独立，不会互相影响。

周五下午，小北完成了筛选功能，测试通过，心情大好。他习惯性地执行了那套熟悉的命令：

```bash
git add .
git commit -m "feat: 添加任务筛选功能"
git push origin main
```

"搞定！"小北伸了个懒腰，准备下班。

与此同时，阿码也在自己的电脑上完成了排序功能。他同样执行了：

```bash
git add .
git commit -m "feat: 添加任务排序功能"
git push origin main
```

然后阿码看到了一行让他心凉的提示：

```
! [rejected]        main -> main (fetch first)
error: failed to push some refs to 'origin'
hint: Updates were rejected because the remote contains work that you do
hint: not have locally.
```

"什么情况？"阿码皱起眉头。他按照提示执行了 `git pull`，然后重新 push。这次成功了。

但周一早上，小北发现不对劲——他周五写的筛选功能代码不见了。`TaskManager` 类里只剩下阿码的排序功能代码。

"阿码！你把我代码弄丢了！"小北在群里喊。

"我没有啊，"阿码一脸无辜，"我只是 push 了我的代码..."

### 问题出在哪

老潘看到群里消息，叹了口气，把两人叫到会议室。

"你们两个都在 main 分支上直接开发，"老潘在白板上画了一条线，"这是 main 分支的时间线。小北周五下午提交了一次，阿码周五晚上又提交了一次——但阿码提交前没有拉取小北的最新代码。"

他在白板上画了两个分叉的箭头：

```
main: A --- B(小北) --- C(阿码覆盖)
              \
               (小北的代码丢了)
```

"当阿码 push 时，Git 拒绝了，因为远程仓库有小北的提交。阿码执行了 `git pull`，Git 自动做了合并——但合并策略可能保留了阿码的版本，覆盖了小北的改动。"

小北不解："那怎么办？难道两个人不能同时开发吗？"

"能，但必须用分支。"老潘又画了一张图，

```
main:      A ------------------- C(合并)
            \                 /
feature-filter: B1 --- B2 ---
            \                 /
feature-sort:   D1 --- D2 ---
```

"看，小北在 `feature-filter` 分支开发，阿码在 `feature-sort` 分支开发。各自开发完成后，通过 Pull Request 合并回 main。这样两个人的代码不会互相覆盖，而且合并前可以互相审查。"

小北恍然大悟："所以分支就像平行宇宙？每个人都在自己的宇宙里开发，互不干扰，最后再把成果合并到一起？"

"完全正确，"老潘点头，"分支是 Git 最强大的功能之一，也是团队协作的基础。"

### 分支的本质

分支是什么？从技术角度说，分支只是一个指向某次提交的指针。但从协作角度说，**分支是一种"隔离机制"**——它让你可以在不影响主线的情况下进行实验性开发。

想象一下，如果没有分支：
- 你正在开发一个新功能，代码写了一半，还不稳定
- 这时线上出现了一个紧急 bug，需要立即修复
- 但你现在的代码是"半成品"状态，无法直接发布
- 你该怎么办？

有了分支，问题迎刃而解：
- 你在 `feature-xxx` 分支开发新功能，随意提交，不用担心破坏主线
- 紧急 bug 直接在 main 分支修复并发布
- 新功能完成后，再合并回 main

这就是 **Feature Branch 工作流**的核心思想：**任何功能开发都在独立分支进行，main 分支始终保持可发布状态**。

---

## 2. Feature Branch 工作流——各自开发，安全合并

小北吸取了代码被覆盖的教训，决定试试老潘说的分支工作流。他拉着阿码一起研究怎么在"平行宇宙"里开发，再安全地把成果合并回来。

### 创建和切换分支

小北要为"任务筛选功能"创建一个分支：

```bash
# 查看当前分支
$ git branch
* main

# 创建并切换到新分支
$ git checkout -b feature/task-filter
Switched to a new branch 'feature/task-filter'

# 确认当前分支
$ git branch
  main
* feature/task-filter
```

`git checkout -b` 是 `git branch` + `git checkout` 的简写。现在小北在 `feature/task-filter` 分支上，他可以随意修改代码，不会影响 main 分支。

### 在分支上开发

小北开始实现筛选功能：

```java
// 文件：TaskManager.java
public class TaskManager {
    private List<Task> tasks = new ArrayList<>();

    // 新增：按优先级筛选任务
    public List<Task> filterByPriority(String priority) {
        List<Task> result = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getPriority().equals(priority)) {
                result.add(task);
            }
        }
        return result;
    }

    // 新增：按完成状态筛选任务
    public List<Task> filterByStatus(boolean completed) {
        List<Task> result = new ArrayList<>();
        for (Task task : tasks) {
            if (task.isCompleted() == completed) {
                result.add(task);
            }
        }
        return result;
    }

    // ... 其他方法
}
```

开发完成后，小北提交到当前分支：

```bash
$ git add src/main/java/TaskManager.java
$ git commit -m "feat: 添加任务筛选功能（按优先级和状态）"

$ git log --oneline
a1b2c3d feat: 添加任务筛选功能（按优先级和状态）
4e5f6g7 fix: 修复任务标题验证逻辑
8h9i0j1 docs: 更新 README
```

注意：这些提交只在 `feature/task-filter` 分支上，main 分支完全不受影响。

### 推送分支到远程

为了让队友能看到自己的代码，小北需要把分支推送到远程仓库：

```bash
$ git push -u origin feature/task-filter
```

`-u`（或 `--set-upstream`）参数建立本地分支和远程分支的关联。之后小北可以直接用 `git push` 推送更新。

### 老潘的工程视角

老潘看到小北的操作，走过来补充："在公司里，Feature Branch 工作流是强制要求。为什么？"

他掰着手指列举：

"第一，main 分支必须随时可发布。如果直接在 main 上开发，半成品代码会污染主线，导致无法紧急发布。"

"第二，代码审查需要上下文。Pull Request 展示了分支的所有改动，审查者可以一目了然地看到'这个功能的完整实现'，而不是从 main 的历史里翻找。"

"第三，回滚更容易。如果某个功能出了问题，直接回滚那次合并即可，不影响其他功能。"

"第四，并行开发。你们团队有三个人，可以同时开三个 feature 分支，互不干扰。"

小北问："那分支命名有什么规范吗？"

"有，"老潘说，"常见的命名规范：
- `feature/xxx`：新功能
- `bugfix/xxx`：bug 修复
- `hotfix/xxx`：紧急修复（直接从 main 切出）
- `docs/xxx`：文档更新

用斜杠分隔，清晰明了。"

### 合并分支

小北的功能开发完成，测试通过，现在要把代码合并回 main。

```bash
# 先切换到 main 分支
$ git checkout main

# 拉取最新代码（确保 main 是最新的）
$ git pull origin main

# 合并 feature 分支
$ git merge feature/task-filter
```

如果一切顺利，Git 会创建一个"合并提交"（merge commit），把两个分支的历史连接起来。

但现实中往往不会这么顺利——如果阿码同时修改了 `TaskManager.java`，就可能出现**冲突**。

### 处理合并冲突

假设阿码在 main 分支上修改了 `TaskManager` 的构造函数，而小北在自己的分支上也修改了同一个地方。合并时 Git 会提示：

```
Auto-merging src/main/java/TaskManager.java
CONFLICT (content): Merge conflict in src/main/java/TaskManager.java
Automatic merge failed; fix conflicts and then commit the result.
```

打开文件，小北看到：

```java
<<<<<<< HEAD
    public TaskManager() {
        this.tasks = new ArrayList<>();
        this.validator = new TaskValidator();
    }
=======
    public TaskManager() {
        this.tasks = new ArrayList<>();
    }

    public List<Task> filterByPriority(String priority) {
        // ...
    }
>>>>>>> feature/task-filter
```

`<<<<<<< HEAD` 到 `=======` 是 main 分支的内容，`=======` 到 `>>>>>>> feature/task-filter` 是小北分支的内容。

小北需要手动编辑，保留双方需要的代码：

```java
    public TaskManager() {
        this.tasks = new ArrayList<>();
        this.validator = new TaskValidator();
    }

    public List<Task> filterByPriority(String priority) {
        // ...
    }
```

然后标记冲突已解决：

```bash
$ git add src/main/java/TaskManager.java
$ git commit -m "merge: 合并 feature/task-filter 分支，解决构造函数冲突"
```

冲突处理是团队协作的常态。好消息是，如果你使用 Pull Request（下一节讲），冲突会在合并前被发现，可以在网页上直观地解决。

---

> **AI 时代小专栏：AI 辅助 Code Review 的边界**
>
> 2026 年，AI 代码审查工具的市场规模已从 2025 年的 5.5 亿美元飙升至 40 亿美元，50% 的专业开发者已在生产环境中部署 AI 工具。GitHub Copilot 用户数突破 2000 万，市场占有率达 42%。使用 AI 代码审查工具的团队，Pull Request 处理时间从平均 9.6 天骤降至 2.4 天——减少了 75%。
>
> 数据看起来很美。但等等，还有另一组数据。
>
> 安全公司 Checkmarx 的研究发现：AI 生成的代码中，45% 包含安全漏洞，而 Java 代码的失败率高达 72%。29.1% 的 Python 代码（由 Copilot 生成）包含潜在安全弱点。开源模型引入漏洞的频率是商业工具的 4 倍。
>
> 这意味着什么？
>
> **AI 擅长处理"已知的已知"**：语法错误、风格问题、常见漏洞模式、空指针风险。这些正是它能把 PR 处理时间压缩 75% 的原因——机械性检查被自动化了。
>
> **但 AI 不擅长处理"已知的未知"和"未知的未知"**：
> - 它无法判断"这个类是否符合单一职责原则"——这需要理解业务上下文
> - 它不知道你的项目整体架构，无法判断"这个改动是否破坏了架构一致性"
> - 它无法验证"这个计算逻辑是否符合业务规则"
> - 它可以检查"是否有注释"，但无法判断"这段代码是否容易理解"
>
> 更重要的是，Code Review 不只是"找 bug"，还是**知识分享和团队对齐**的过程。审查者通过评论解释"为什么这样设计更好"，被审查者通过回复澄清意图——这种互动是 AI 无法替代的。
>
> 所以正确的做法是：**AI 辅助 + 人类决策**。让 AI 处理机械性检查，人类专注于设计决策和业务逻辑。就像你刚学的 Pull Request 流程——AI 可以帮你预审，但合并按钮必须由人按下。
>
> 参考（访问日期：2026-02-11）：
> - [Is GitHub Copilot Worth It? Real-World Data Reveals the Answer](https://www.faros.ai/blog/is-github-copilot-worth-it-real-world-data-reveals-the-answer)
> - [Best AI Code Review Tools 2026](https://www.qodo.ai/blog/best-ai-code-review-tools-2026/)
> - [AI Is Writing Your Code: Who's Keeping It Secure?](https://checkmarx.com/blog/ai-is-writing-your-code-whos-keeping-it-secure/)

---

## 3. Pull Request——代码审查的正式入口

小北的功能分支开发完成，准备合并到 main。但按照团队规范，他不能直接 `git merge`，必须先创建 **Pull Request**（PR）。

### 什么是 Pull Request

Pull Request 是"请求合并"的正式流程。它不是 Git 本身的功能，而是 GitHub、GitLab 等平台提供的协作机制。

PR 的核心价值：
1. **代码审查的入口**：在合并前，让队友审查代码
2. **变更的展示窗口**：清晰地展示"这个分支改了什么"
3. **讨论的记录**：所有审查意见和回复都被记录，可追溯
4. **自动化检查**：可以集成 CI/CD，自动运行测试

阿码不理解："我直接 `git merge` 不是更快吗？为什么要多这一步？"

老潘回答："直接 merge 的问题是——一旦代码进入 main，就很难撤回了。如果代码有严重问题，可能已经影响到其他人。PR 是在'放行'前设置的一道关卡。"

### 创建 Pull Request

小北在 GitHub 上点击 "New Pull Request"，填写 PR 描述：

```markdown
## 改动内容
添加任务筛选功能，支持按优先级和完成状态筛选任务。

### 新增方法
- `filterByPriority(String priority)`：返回指定优先级的任务列表
- `filterByStatus(boolean completed)`：返回指定状态的任务列表

### 代码示例
```java
TaskManager manager = new TaskManager();
// 添加一些任务...

List<Task> highPriorityTasks = manager.filterByPriority("高");
List<Task> incompleteTasks = manager.filterByStatus(false);
```

## 测试方式
1. 运行 `TaskManagerTest` 中的测试用例
2. 手动测试：在 CLI 中创建任务后使用筛选功能

## 审查清单
- [ ] 代码符合 Java 命名规范
- [ ] 新增方法有适当的注释
- [ ] 异常处理完善（考虑空输入情况）
- [ ] 单元测试覆盖新增功能
```

好的 PR 描述应该让审查者快速理解：
- **做了什么**：改动的目标和范围
- **为什么做**：业务背景或问题背景
- **怎么做**：实现思路
- **如何测试**：验证改动的步骤
- **审查重点**：希望审查者特别关注的地方

### 审查清单的应用

小北在 PR 描述中加了一个"审查清单"（Review Checklist）。这是 Code Review 的系统化工具——确保审查者不会遗漏重要检查项。

阿码作为审查者，打开 PR 页面，看到 Files changed 标签页显示了所有改动。他开始逐项检查：

**检查项 1：代码是否符合 SOLID 原则**

阿码盯着 `TaskManager` 类，突然举手："等等，Week 02 学的单一职责原则——这个类现在是不是管得太宽了？"

他检查代码：

```java
// 小北的代码
public class TaskManager {
    private List<Task> tasks = new ArrayList<>();

    public List<Task> filterByPriority(String priority) {
        // ...
    }

    public List<Task> filterByStatus(boolean completed) {
        // ...
    }

    public void addTask(Task task) { /* ... */ }
    public void removeTask(String title) { /* ... */ }
    public void markCompleted(String title) { /* ... */ }
}
```

"等等，"阿码在 PR 下评论，"`TaskManager` 现在有 6 个方法了——添加、删除、标记完成、筛选... 它是不是承担了太多职责？按照 Week 02 学的 SRP，筛选功能是否应该拆分到单独的 `TaskFilter` 类？"

小北回复："你说得对，但我现在只是添加两个筛选方法，拆分会增加复杂度。我可以在下一次重构时再拆分吗？"

阿码思考了一下："可以，但请在代码里加一个 TODO 注释，说明这个类需要重构。另外，ADR-001 里应该记录这个技术债。"

这就是审查清单的价值——它不是一张冷冰冰的检查表，而是把前几周学的 SOLID 原则、异常处理策略真正应用到工程实践中的抓手。当阿码提到 SRP 时，他不是在背定义，而是在判断"这个设计是否还能承受未来的扩展"。

**检查项 2：异常处理是否完善**

阿码继续检查，这次关注 Week 03 学的异常处理：

```java
public List<Task> filterByPriority(String priority) {
    List<Task> result = new ArrayList<>();
    for (Task task : tasks) {
        if (task.getPriority().equals(priority)) {  // ← 潜在 NPE
            result.add(task);
        }
    }
    return result;
}
```

阿码评论："如果 `priority` 参数为 null，`task.getPriority().equals(priority)` 会抛出 NPE。建议添加前置检查："

```java
public List<Task> filterByPriority(String priority) {
    if (priority == null) {
        throw new IllegalArgumentException("优先级不能为 null");
    }
    // ...
}
```

小北回复："收到，我立即修改。"

阿码又追问了一个刁钻的问题："如果 `task.getPriority()` 返回 null 呢？`equals()` 调用会不会也炸？"

小北一愣："呃... 如果 Task 对象本身的数据有问题，那应该是 Task 类的责任去保证吧？"

"对，"阿码点头，"但 `filterByPriority` 作为公共 API，应该对任何输入都保持健壮——不管是参数为 null，还是列表里的 Task 有脏数据。这才是 Week 03 学的防御式编程嘛。"

**检查项 3：输入验证是否完整**

阿码继续检查防御式编程：

```java
// 小北的代码缺少对非法优先级值的验证
// 如果传入 "极高"，而系统只支持 "高/中/低"，会发生什么？
```

阿码评论："建议验证 `priority` 的值是否在允许的范围内（高/中/低），而不是只检查 null。"

### PR 的合并

小北根据阿码的意见修改了代码，重新推送。阿码确认修改后，点击 "Approve"（批准）。

现在小北可以合并 PR 了。GitHub 提供了几种合并方式：

1. **Create a merge commit**：保留分支历史，创建一个合并提交
2. **Squash and merge**：把分支的所有提交压缩成一个提交，再合并
3. **Rebase and merge**：把分支的提交变基到 main 上，保持线性历史

对于功能分支，通常选择 "Squash and merge"——把一个功能的多个提交（"WIP"、"fix bug"、"refactor"）压缩成一个干净的提交，方便后续追溯。

合并后，小北删除 feature 分支：

```bash
$ git checkout main
$ git pull origin main  # 拉取合并后的代码
$ git branch -d feature/task-filter  # 删除本地分支
$ git push origin --delete feature/task-filter  # 删除远程分支
```

---

## 4. Code Review 实践——如何审查他人的代码

阿码作为审查者，需要系统地评估小北的代码。但"审查"不只是"挑错"，而是"一起让代码更好"。

### 审查什么

老潘给阿码一份审查清单，涵盖了从设计到实现的各个方面：

**设计层面**
- 这个改动是否符合整体架构？
- 类和方法的职责是否清晰？（SRP 检查）
- 是否有更好的设计方案？

**代码层面**
- 是否有明显的 bug？（空指针、数组越界等）
- 异常处理是否完善？（Week 03 的知识）
- 输入验证是否到位？（防御式编程）
- 是否有重复代码？

**可维护性**
- 命名是否清晰？（方法名、变量名、类名）
- 是否有适当的注释？（解释"为什么"，不是"做什么"）
- 代码是否容易理解？（复杂度是否过高）

**测试**
- 是否有单元测试？
- 测试是否覆盖了边界情况？
- 测试是否独立、可重复？

### 如何给出反馈

阿码发现小北的代码有一个问题：

```java
// 小北的代码
public List<Task> filterByPriority(String priority) {
    List<Task> result = new ArrayList<>();
    for (Task task : tasks) {
        if (task.getPriority().equals(priority)) {
            result.add(task);
        }
    }
    return result;
}
```

不好的反馈方式：

> "这里会 NPE，你代码写错了。"

好的反馈方式：

> "如果 `priority` 为 null，第 5 行会抛出 NPE。建议添加前置检查，或者考虑使用 `Objects.equals()` 来避免空指针。你觉得哪种方式更适合这个场景？"

区别在于：
- **提问而非指责**："如果...会怎样？"比"你错了"更容易接受
- **解释原因**：说明为什么这是个问题（NPE 风险）
- **提供选项**：给出可能的解决方案，让作者选择
- **保持尊重**：承认作者可能有更好的理由

### 阿码的疑问

阿码在审查过程中突然想到一个问题："这些审查清单这么长，为什么不直接用 AI 生成审查清单？"

老潘正好路过："好问题。AI 确实可以生成审查清单，但它生成的只是'通用模板'，不是'针对这个 PR 的检查项'。"

"比如这个 PR 修改了 `TaskManager`，AI 可能生成'检查是否有单元测试'——这没错，但它不会知道'这个类的筛选逻辑是否应该拆分到单独的 Filter 类'——这需要理解业务上下文和架构演进方向。"

"更重要的是，"老潘强调，"Code Review 是**学习的机会**。审查者通过审查别人的代码学习新的写法，被审查者通过反馈改进自己的技能。如果完全交给 AI，这种知识传递就消失了。"

阿码点头："所以 AI 可以辅助，但不能替代？"

"对。让 AI 帮你检查'是否有明显的语法错误'、'是否符合基本规范'，但设计决策、业务逻辑正确性、代码可读性——这些必须由人来判断。"

### 审查中的技术分歧

阿码和小北在 PR 讨论中产生了分歧。阿码认为应该把筛选功能拆分到 `TaskFilter` 类：

```java
// 阿码建议的设计
public class TaskFilter {
    public List<Task> byPriority(List<Task> tasks, String priority) { /* ... */ }
    public List<Task> byStatus(List<Task> tasks, boolean completed) { /* ... */ }
}
```

小北则认为保持现状更简单：

```java
// 小北的设计
public class TaskManager {
    public List<Task> filterByPriority(String priority) { /* ... */ }
    public List<Task> filterByStatus(boolean completed) { /* ... */ }
}
```

两人在 PR 下讨论了几轮，各执己见。这时老潘介入了。

"你们两个都没错，只是权衡点不同。"老潘说，"阿码考虑的是 SRP 和可测试性，小北考虑的是简单性和当前需求。"

"在 Code Review 中，这种分歧很常见。关键是**如何讨论**而不是**谁赢**。"

老潘给出了处理技术分歧的建议：

1. **明确目标和约束**："我们的首要目标是可维护性还是快速交付？"
2. **量化而非定性**："拆分成 Filter 类会增加 3 个类文件，但能让测试覆盖率提高 20%"——比"这样更好"更有说服力
3. **寻找折中方案**："现在保持现状，但在 ADR 里记录'当筛选逻辑超过 5 个时拆分到 Filter 类'"
4. **升级决策**：如果无法达成一致，由首席架构师做最终决定（并记录在 ADR 中）

最终，小北和阿码达成妥协：保持当前设计，但在代码中添加 TODO 注释，并在 ADR-002 中记录重构计划。

---

> **AI 时代小专栏：团队协作中的 AI 使用规范**
>
> AI 编程工具已经无处不在，但"用 AI"和"用好 AI"是两回事。2025 年的行业数据显示：**未经 AI 工具培训的团队，生产力提升比受培训团队低 60%**。换句话说，给团队一把 AI 锤子，如果不教他们怎么用，反而可能砸到自己的脚。
>
> 这引出了 2025 年行业共识：**Human-in-the-Loop (HITL)**——AI 应该是加速器，而非自动驾驶。企业需要对 AI 生成代码采取"零信任"态度，将其视为需要同样严格审查的"不可信输入"。
>
> 一个成熟的团队 AI 使用公约通常包括：
>
> **明确允许的工具和用途**
> - 指定团队使用的 AI 工具（如 GitHub Copilot、特定版本的模型）
> - 定义可接受的使用场景：原型开发可以用 AI 加速，生产代码必须经过人工审查
> - 敏感组件（认证、金融系统）限制或禁止使用 AI 生成代码
>
> **强制同行审查**
> - 所有 AI 辅助代码必须进行同行审查，且审查者需额外关注边界情况
> - 在 PR 描述中明确标注"AI 辅助生成"的部分
> - 审查清单中增加"AI 生成代码安全性检查"专项
>
> **合规框架**
> - 遵循 NIST AI RMF（风险管理框架）
> - 符合 EU AI Act 的全球合规要求
> - 参考 ISO 42001（AI 管理系统）建立内部流程
>
> 老潘的建议："AI 是工具，不是替代者。公约的目的是**明确责任边界**——AI 辅助你写代码，但你为代码质量负责。就像你刚学的 Code Review 流程：无论代码是人写的还是 AI 写的，合并前都必须经过同样的审查标准。"
>
> 参考（访问日期：2026-02-11）：
> - [AI Code Enterprise Adoption Guide](https://getdx.com/blog/ai-code-enterprise-adoption/)
> - [AI Coding Series: Part 3 - Security Guidelines](https://techbytes.app/guides/ai-coding-series/part-3/)
> - [AI Is Writing Your Code: Who's Keeping It Secure?](https://checkmarx.com/blog/ai-is-writing-your-code-whos-keeping-it-secure/)

---

## 5. 建设性反馈——如何处理技术分歧

CampusFlow 团队正在讨论一个技术决策：是否使用 Lombok 来减少样板代码。

阿码支持使用 Lombok："它可以自动生成 getter、setter、toString，减少很多重复代码。"

小北反对："但它隐藏了实现细节，新人可能不知道 `@Data` 注解到底生成了什么方法。而且 Week 02 我们强调过'手写 getter/setter 有助于理解封装'。"

讨论逐渐升温，气氛变得有些紧张。

### 老潘的故事

老潘看到讨论走向，讲了一个故事：

"三年前，我带的一个团队，Code Review 做得很差。不是不做，而是做得'很伤人'。"

"审查者用红色的 'X' 标记每一个问题，语气像老师批改作业：'这里错了'、'不符合规范'、'重写'。被审查者感觉被攻击了，开始防御性回复：'你不懂业务'、'之前就是这样写的'。"

"结果呢？PR 讨论变成了争吵，开发者开始避免提交 PR——宁可自己多测几遍，也不愿意被'公开处刑'。代码质量没有提升，团队氛围却恶化了。"

"后来出了一次线上事故。一个明显的空指针 bug 通过了 Code Review，因为审查者和被审查者当时正在争论另一个问题，都忽略了这行代码。"

老潘停顿了一下："Code Review 的目的是**提高代码质量**，不是**证明谁更聪明**。如果审查过程让团队成员感到被攻击，它就失败了。"

### 建设性反馈的技巧

老潘分享了几个给出建设性反馈的技巧：

**1. 对事不对人**

不好的说法："你这里写错了。"

好的说法："这段代码可能会导致 NPE，建议添加 null 检查。"

**2. 解释原因，不只是指出问题**

不好的说法："不要用魔法数字。"

好的说法："这里的 '3' 是最大优先级值，建议提取为常量 `MAX_PRIORITY`，这样如果业务规则变化（比如支持 5 级优先级），只需要改一个地方。"

**3. 承认不确定性**

"我不确定这个设计在并发场景下是否安全，你能解释一下吗？"

"我想到一个可能的边界情况：如果列表为空，这个方法会返回什么？"

**4. 提供选项，让对方选择**

"这里有两种处理方式：A 方案简单直接，B 方案更灵活但复杂。考虑到我们目前的需求，我倾向于 A，你觉得呢？"

**5. 肯定好的部分**

"这个异常处理很完善，考虑了多种错误情况。"

"测试用例覆盖了边界情况，做得很好。"

### 处理分歧的决策流程

当技术分歧无法通过讨论达成一致时，需要正式的决策流程：

1. **各自整理观点**：双方用书面形式阐述自己的方案、优缺点、适用场景
2. **收集更多信息**：查找业界最佳实践、类似项目的经验、性能测试数据
3. **小规模实验**：如果可能，两个方案都实现一个原型，对比实际效果
4. **首席架构师决策**：由首席架构师做最终决定，并记录在 ADR 中
5. **团队承诺**：一旦决策做出，即使反对者也要全力执行

老潘强调："决策记录比决策结果更重要。即使后来证明决策是错的，ADR 也能告诉我们'当初为什么这样决定'——这能避免重复犯错。"

### 技术分歧的化解之道

小北和阿码的 Lombok 之争，其实是软件工程中每天都会上演的剧本。老潘后来补充了一个观点："技术分歧的本质，往往不是'对错'之争，而是'权衡'之辩。"

"阿码看重的是开发效率和代码简洁，小北看重的是可理解性和团队成长——两种价值观都有道理。Code Review 中的分歧处理，关键不是说服对方，而是："

"第一，**把隐性的假设显性化**。当你说'这样更好'时，你默认了什么前提？是'团队规模会快速增长'，还是'这段代码会被频繁修改'？不同的前提，会导向不同的最优解。"

"第二，**量化不确定的因素**。'增加复杂度'到底是增加了多少？'提高可读性'又能减少多少 onboarding 时间？用数据说话，而不是用嗓门说话。"

"第三，**接受'没有完美答案'的现实**。软件工程充满了 trade-off。今天的最佳实践，可能是明天的技术债。重要的是记录决策过程，而不是追求决策完美。"

### CampusFlow 的 Lombok 决策

最终，CampusFlow 团队对 Lombok 问题达成以下决策：

```markdown
# 决策：暂不引入 Lombok

## 理由
1. 教学阶段，手写 getter/setter 有助于理解封装原理
2. 团队规模小，样板代码量可控
3. 避免引入额外依赖（Lombok 需要 IDE 插件支持）

## 替代方案
- 使用 IDE 自动生成 getter/setter（IntelliJ: Alt+Insert）
- 将来团队规模扩大后，重新评估 Lombok

## 记录
- 提议者：阿码
- 反对者：小北
- 决策者：首席架构师（小北）
- 日期：2026-02-11
```

这个决策被记录在团队的开发规范文档中，三个月后将重新评估。

老潘在文档末尾加了一句话："所有规范都是活的文档。当团队规模、技术栈或业务需求发生变化时，勇于重新审视过去的决策——这才是工程成熟度的体现。"

---

## 6. ADR-002：数据存储方案决策

Lombok 之争让团队意识到记录决策的重要性——当时如果有个书面记录，就不会反复争论同样的问题。现在，另一个更大的决策摆在面前：CampusFlow 的数据存储方案。

目前所有数据都保存在内存中（`ArrayList<Task>`），程序关闭后数据就丢失了。团队需要选择一个持久化方案。

### 方案对比

首席架构师小北主持了讨论，三个方案被提出：

**方案 A：内存存储（现状）**
- 优点：简单、快速、无需额外依赖
- 缺点：程序关闭后数据丢失
- 适用：原型开发、演示

**方案 B：文件存储（JSON/CSV）**
- 优点：简单易实现、数据可读、无需数据库
- 缺点：并发访问困难、查询效率低、无事务支持
- 适用：单机应用、数据量小

**方案 C：SQLite 数据库**
- 优点：支持 SQL 查询、事务、并发控制、数据完整性
- 缺点：需要学习 SQL、增加复杂度
- 适用：需要持久化和查询能力的应用

阿码倾向于方案 B："我们先用文件存储，简单快速。等将来需要数据库时再迁移。"

老潘提出不同意见："根据我的经验，'等将来再迁移'往往意味着'永远不会迁移'，或者'迁移时很痛苦'。你们 Week 02 设计的 `TaskRepository` 接口，不就是为了隔离存储实现吗？"

小北恍然大悟："对！`TaskRepository` 是接口，我们可以先实现一个 `FileTaskRepository`，将来再换成 `SQLiteTaskRepository`，业务代码完全不用改。"

### 撰写 ADR-002

基于讨论，小北撰写了 ADR-002：

```markdown
# ADR-002: 数据存储方案决策

## 状态
已采纳

## 背景
CampusFlow 目前使用内存存储（ArrayList），程序关闭后数据丢失。
需要选择一种持久化方案，支持数据长期保存和基本查询。

## 决策
我们选择 **SQLite** 作为数据存储方案。

### 实现路径
1. Week 04-05：实现 FileTaskRepository（JSON 文件存储）
   - 作为过渡方案，验证 Repository 接口设计
   - 支持基本 CRUD 操作

2. Week 06-07：实现 SQLiteTaskRepository
   - 替换 FileTaskRepository
   - 利用 SQL 支持复杂查询（按日期范围筛选等）

### 架构设计
```
┌─────────────────┐
│   TaskManager   │
├─────────────────┤
│ - repository:   │
│   TaskRepository│
└────────┬────────┘
         │ 依赖接口
         ▼
┌─────────────────┐
│ TaskRepository  │ ← 接口
│  (interface)    │
└────────┬────────┘
         │ 两种实现
    ┌────┴────┐
    ▼         ▼
┌────────┐ ┌──────────┐
│  File  │ │  SQLite  │
│Repository│ │Repository│
└────────┘ └──────────┘
```

## 理由

### 为什么选 SQLite（而不是内存或纯文件）

1. **数据持久化**：程序关闭后数据不丢失（优于内存）
2. **查询能力**：支持 SQL 查询，便于实现复杂筛选（优于纯文件）
3. **事务支持**：保证数据一致性（优于纯文件）
4. **轻量级**：无需单独安装数据库服务器，适合教学项目
5. **可扩展性**：将来可迁移到 PostgreSQL/MySQL 等生产数据库

### 为什么先实现 FileRepository

1. **渐进式演进**：先验证接口设计，再引入数据库复杂度
2. **教学价值**：让学生理解"接口隔离实现"的好处
3. **回滚能力**：如果 SQLite 实现遇到问题，可以快速回退到文件存储

## 替代方案

### 方案 A：内存存储（不持久化）
- **优点**：最简单
- **缺点**：数据丢失，无法用于真实场景
- **结论**：不采纳——项目目标是可交付的应用，不是演示原型

### 方案 B：纯 JSON 文件存储
- **优点**：实现简单、数据可读
- **缺点**：无查询能力、无事务、并发困难
- **结论**：作为过渡方案采纳，但不是最终方案

### 方案 C：MySQL/PostgreSQL
- **优点**：生产级数据库，功能完善
- **缺点**：需要安装和配置，增加环境复杂度
- **结论**：暂不采纳——超出教学项目范围，Week 08+ 可重新评估

## 后果

### 正面影响
- 数据持久化，支持真实使用场景
- Repository 接口设计得到验证
- 学生掌握 JDBC 和 SQL 基础

### 负面影响
- 增加学习成本（SQL、JDBC）
- 需要处理数据库连接、异常等额外复杂度
- 测试需要设置数据库环境

### 风险与缓解

| 风险 | 可能性 | 影响 | 缓解措施 |
|------|--------|------|----------|
| JDBC 学习曲线陡峭 | 中 | 中 | 提供示例代码和模板 |
| 数据库环境配置问题 | 高 | 低 | 使用 SQLite（嵌入式，无需安装） |
| SQL 注入漏洞 | 中 | 高 | Week 06 专门讲解参数化查询 |

## 验证方式
- [ ] Week 05：FileTaskRepository 实现完成，数据可保存到文件
- [ ] Week 06：SQLiteTaskRepository 实现完成，功能与 File 版本一致
- [ ] Week 07：数据迁移测试（File → SQLite）
- [ ] Week 08：性能对比（File vs SQLite）

## 相关决策
- ADR-001：领域模型设计（定义了 TaskRepository 接口）
- ADR-003（Week 06）：API 设计决策

## 首席架构师
小北 - 第 1 轮（Week 02-03）

## 记录日期
2026-02-11
```

### ADR 必须由人写

阿码问："ADR 能用 AI 生成吗？"

老潘严肃地回答："**绝对不能。**"

阿码有些意外："为什么？AI 写得又快又好，格式还规范。"

"格式是表象，思考是本质。"老潘解释道，"AI 可以帮你整理格式、检查语法，但决策内容必须由人写。因为：

1. **ADR 记录的是你的团队的决策过程**——为什么选 A 不选 B？你们的业务约束是什么？AI 不知道这些。

2. **写 ADR 的过程就是深度思考的过程**。当你被迫用文字解释'为什么这样设计'时，你会发现自己思维的漏洞。

3. **ADR 是团队的共识**，不是一个人的想法。它需要团队讨论，不是 AI 能替代的。

AI 可以帮你：
- 生成 ADR 模板
- 检查语法和格式
- 提供类似项目的参考

但**决策本身必须由人做、由人写、由人负责**。

小北点头："我明白了。AI 可以帮我写代码，但决策和责任必须是人来承担。工具再智能，也不能替我做判断。"

---

## CampusFlow 进度

本周 CampusFlow 正式进入团队协作阶段。你们不再是单打独斗的开发者，而是一个有明确分工、有协作流程的团队。

### 团队组建完成

- **首席架构师**：小北（第 1 轮，Week 02-03）
- **后端开发**：小北、阿码
- **职责分工**：小北负责核心领域模型，阿码负责功能扩展

### Git 协作工作流建立

团队采用 Feature Branch 工作流：

```bash
# 1. 从 main 创建功能分支
git checkout -b feature/xxx

# 2. 在分支上开发、提交
git add .
git commit -m "feat: xxx"

# 3. 推送分支到远程
git push -u origin feature/xxx

# 4. 创建 Pull Request，请求合并
# 5. 代码审查通过后，合并到 main
```

### 完成首次 Pull Request 和 Code Review

本周完成了至少一次完整的 PR 流程：
- 小北提交任务筛选功能
- 阿码进行 Code Review，提出改进意见
- 小北根据反馈修改代码
- PR 合并到 main 分支

### ADR-002 提交

团队讨论并确定了数据存储方案：
- 最终方案：SQLite（轻量级数据库）
- 过渡方案：FileRepository（JSON 文件存储）
- 决策依据：持久化需求、查询能力、教学复杂度平衡

ADR-002 文档已提交到 `docs/adr/002-数据存储方案.md`。

### 代码状态

```
campusflow/
├── docs/
│   └── adr/
│       ├── 001-领域模型设计.md
│       └── 002-数据存储方案.md
├── src/
│   └── main/
│       └── java/
│           └── edu/campusflow/
│               ├── domain/
│               │   ├── Task.java
│               │   └── TaskManager.java
│               ├── repository/
│               │   └── TaskRepository.java  # 新增接口
│               └── exception/
│                   └── CampusFlowException.java
└── pom.xml
```

### 回顾桥：本周知识的应用

在 CampusFlow 的协作开发中，你们应用了前几周的知识：

**Week 02 的 SOLID 原则**：在 Code Review 中，审查清单包含"是否符合单一职责原则"的检查。`TaskManager` 的职责边界在审查中被讨论。

**Week 02 的封装**：审查中检查了 `private` 字段和 `getter/setter` 的使用，确保数据访问通过受控的接口。

**Week 03 的异常处理**：审查清单包含"异常处理是否完善"的检查。所有公共方法都检查了前置条件，抛出合适的异常。

**Week 03 的防御式编程**：在 `Task` 类的 setter 中添加了输入验证，防止非法数据进入系统。

---

## Git 本周要点

### 必会命令

```bash
# 分支操作
git branch                          # 查看分支列表
git branch <name>                   # 创建分支
git checkout -b <name>              # 创建并切换分支
git checkout <name>                 # 切换分支
git branch -d <name>                # 删除分支

# 合并操作
git merge <branch>                  # 合并指定分支到当前分支

# 远程操作
git push -u origin <branch>         # 推送分支到远程
git pull origin main                # 拉取远程 main 分支更新

# 查看历史
git log --oneline --graph           # 查看分支历史（图形化）
```

### Commit Message 规范

本周的 commit 示例：

```bash
# 功能开发
git commit -m "feat: 添加任务筛选功能（按优先级）"

# Bug 修复
git commit -m "fix: 修复 filterByPriority 的 NPE 问题"

# 代码审查后的修改
git commit -m "refactor: 根据 Code Review 意见优化异常处理"

# 文档更新
git commit -m "docs: 添加 ADR-002 数据存储方案决策"
```

### 常见坑

**1. 忘记在 feature 分支开发，直接在 main 提交**

后果：半成品代码污染主线，可能影响其他开发者。

解决：养成习惯，任何改动前先 `git checkout -b feature/xxx`。

**2. 合并前没有拉取最新 main，导致冲突**

后果：合并时出现大量冲突，解决困难。

解决：合并前执行 `git pull origin main`，确保基于最新代码。

**3. PR 描述过于简单**

不好的描述："修复 bug"

好的描述："修复 filterByPriority 在 priority 为 null 时的 NPE 问题，添加前置检查并补充单元测试"

**4. Code Review 只关注风格，不关注设计**

不要只评论"这里少了个空格"，更要关注：
- 这个类是否职责过多？
- 异常处理是否完善？
- 是否有更好的设计方案？

---

## 本周小结（供下周参考）

本周是"思维奠基"阶段的收官之周，你们从个人开发走向了团队协作。

**分支工作流**解决了"代码互相覆盖"的问题。小北在第 1 节的遭遇让你明白：没有分支的协作是混乱的。Feature Branch 工作流让每个人在独立的"平行宇宙"中开发，最后通过合并把成果整合。记住那个画面——分支就像平行宇宙，每个人都在自己的宇宙里开发，互不干扰。

**Pull Request** 是代码审查的正式入口。它不只是"请求合并"，是"邀请别人检查我的工作"。好的 PR 描述能让审查者快速理解改动意图，审查清单则确保不遗漏重要检查项。当你写 PR 描述时，想象自己是审查者——你需要看到什么信息才能快速理解这个改动？

**Code Review** 是保障代码质量的关键环节。阿码在第 4 节的疑问引出了一个重要原则：AI 可以辅助审查，但无法替代人类对设计决策的判断。审查不只是"挑错"，是"一起让代码更好"。记住老潘的话：Code Review 的目的是提高代码质量，不是证明谁更聪明。

**建设性反馈**是团队协作的软技能。老潘在第 5 节的故事说明：技术分歧的本质往往不是对错之争，而是权衡之辩。对事不对人、解释原因、提供选项——这些技巧能让技术分歧变成学习机会。当你和队友意见不一致时，试着问："我们各自默认了什么前提？"

**ADR-002** 记录了数据存储方案的决策过程。从内存到文件再到数据库，团队权衡了各种因素，最终选择 SQLite。记住老潘的告诫：ADR 必须由人写，不能用 AI 代劳——它记录的是你的团队的决策过程。更重要的是，决策记录比决策结果更重要，因为今天的最佳实践可能是明天的技术债。

回顾桥贯穿了本周的所有内容：Week 02 的 SOLID 原则体现在审查清单中，Week 03 的异常处理和防御式编程成为代码审查的重点检查项。你们正在把前几周的知识整合到工程实践中。

下周，你们将进入"系统化工程"阶段，开始学习集合抽象和 JUnit 测试——为 CampusFlow 添加更强大的数据管理和质量保障能力。

---

## Definition of Done（学生自测清单）

### 知识理解
- [ ] 能解释 Feature Branch 工作流的优势
- [ ] 能说明 Pull Request 的核心目的
- [ ] 能列出至少 5 项代码审查清单的检查项
- [ ] 能解释 ADR 的作用和基本结构

### 协作能力
- [ ] 能独立创建 feature 分支并完成开发
- [ ] 能创建包含清晰描述的 Pull Request
- [ ] 能使用审查清单审查他人的代码
- [ ] 能给出建设性的代码审查反馈

### 团队项目
- [ ] CampusFlow 团队完成至少一次 feature 分支开发
- [ ] 提交至少一个 Pull Request 并完成 Code Review
- [ ] ADR-002（数据存储方案）提交到 docs/adr/
- [ ] 首席架构师第 1 轮职责完成
