#!/bin/bash
# 示例：Feature Branch 工作流完整演示
#
# 运行方式：
#   1. 创建测试仓库：mkdir /tmp/git-demo && cd /tmp/git-demo && git init
#   2. 执行本脚本：bash chapters/week_04/examples/02_feature_branch.sh
#
# 预期输出：展示完整的 feature branch 工作流程

# ===== 步骤 1：初始化仓库 =====
echo "=== 步骤 1：初始化仓库 ==="
git init
echo "# CampusFlow" > README.md
git add README.md
git commit -m "init: 初始化项目"

# ===== 步骤 2：创建功能分支 =====
echo ""
echo "=== 步骤 2：创建功能分支 ==="

# 查看当前分支
echo "当前分支："
git branch

# 创建并切换到 feature 分支
git checkout -b feature/task-filter

echo "切换后："
git branch

# ===== 步骤 3：在分支上开发 =====
echo ""
echo "=== 步骤 3：在分支上开发 ==="

# 模拟代码修改
cat > TaskManager.java << 'EOF'
import java.util.ArrayList;
import java.util.List;

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
}
EOF

echo "创建 TaskManager.java"

# 提交改动
git add TaskManager.java
git commit -m "feat: 添加任务筛选功能（按优先级）"

# ===== 步骤 4：继续开发，多次提交 =====
echo ""
echo "=== 步骤 4：添加更多功能 ==="

# 添加按状态筛选
cat >> TaskManager.java << 'EOF'

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
EOF

git add TaskManager.java
git commit -m "feat: 添加按状态筛选功能"

# ===== 步骤 5：推送分支到远程 =====
echo ""
echo "=== 步骤 5：推送分支到远程 ==="
echo "git push -u origin feature/task-filter"

# ===== 步骤 6：查看提交历史 =====
echo ""
echo "=== 步骤 6：提交历史 ==="
git log --oneline

# ===== 步骤 7：合并到 main =====
echo ""
echo "=== 步骤 7：合并到 main ==="

git checkout main
git merge feature/task-filter --no-ff -m "merge: 合并任务筛选功能"

echo ""
echo "=== 合并后的历史 ==="
git log --oneline --graph

# ===== 步骤 8：删除功能分支 =====
echo ""
echo "=== 步骤 8：清理分支 ==="
git branch -d feature/task-filter
git branch

echo ""
echo "=== Feature Branch 工作流完成 ==="
echo ""
echo "总结："
echo "  1. git checkout -b feature/xxx  - 创建功能分支"
echo "  2. 开发并提交                   - 在分支上工作"
echo "  3. git push -u origin feature/xxx - 推送分支"
echo "  4. 创建 Pull Request            - 请求代码审查"
echo "  5. 审查通过后合并               - 合并到 main"
echo "  6. git branch -d feature/xxx    - 删除已合并分支"
