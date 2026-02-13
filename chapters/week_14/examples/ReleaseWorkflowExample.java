package examples;

import com.campusflow.util.Version;

/**
 * ReleaseWorkflowExample - 发布流程示例
 *
 * 展示 CampusFlow 的完整发布流程
 */
public class ReleaseWorkflowExample {

    public static void main(String[] args) {
        System.out.println("=== CampusFlow 发布流程示例 ===\n");

        // 当前开发版本
        Version currentVersion = Version.parse("0.9.0");
        System.out.println("当前开发版本: " + currentVersion);

        // 步骤 1: 版本升级决策
        System.out.println("\n【步骤 1】版本升级决策");
        System.out.println("检查变更内容:");
        System.out.println("  - 新增功能? 是");
        System.out.println("  - 破坏性变更? 否");
        System.out.println("  - Bug 修复? 否");
        System.out.println("决策: 升级 MINOR 版本 (0.9.0 -> 1.0.0)");

        // 步骤 2: 创建发布版本
        Version releaseVersion = new Version(1, 0, 0);
        System.out.println("\n【步骤 2】创建发布版本: " + releaseVersion);

        // 步骤 3: 打包
        System.out.println("\n【步骤 3】打包应用");
        System.out.println("运行命令: mvn clean package");
        System.out.println("生成文件: target/campusflow-1.0.0.jar");

        // 步骤 4: 测试
        System.out.println("\n【步骤 4】测试验证");
        System.out.println("运行命令: mvn test");
        System.out.println("确保所有测试通过");

        // 步骤 5: Git 标签
        System.out.println("\n【步骤 5】创建 Git 标签");
        System.out.println("运行命令:");
        System.out.println("  git add .");
        System.out.println("  git commit -m \"Release v1.0.0: Initial public release\"");
        System.out.println("  git tag -a v1.0.0 -m \"Release v1.0.0\"");
        System.out.println("  git push origin main --tags");

        // 步骤 6: 部署
        System.out.println("\n【步骤 6】部署到生产环境");
        System.out.println("设置环境变量:");
        System.out.println("  export CAMPUSFLOW_ENV=prod");
        System.out.println("  export DB_PATH=/var/data/campusflow.db");
        System.out.println("运行应用:");
        System.out.println("  java -jar campusflow-1.0.0.jar");

        // 步骤 7: 版本历史
        System.out.println("\n【步骤 7】版本历史");
        printVersionHistory();

        // 步骤 8: 后续维护
        System.out.println("\n【步骤 8】后续维护");
        System.out.println("Bug 修复: " + releaseVersion.incrementPatch());
        System.out.println("新功能: " + releaseVersion.incrementMinor());
        System.out.println("破坏性变更: " + releaseVersion.incrementMajor());
    }

    private static void printVersionHistory() {
        System.out.println("v1.0.0 (2026-01-15)");
        System.out.println("  - 初始发布");
        System.out.println("  - 功能：创建任务、编辑任务、标记完成");
        System.out.println("  - RESTful API with OpenAPI documentation");
        System.out.println("  - SQLite 持久化");
        System.out.println();
        System.out.println("v1.0.1 (预计 2026-01-20)");
        System.out.println("  - PATCH: Bug 修复");
        System.out.println("  - 修复：任务标题超过 100 字符时不再截断");
        System.out.println("  - 风险：低，可直接升级");
        System.out.println();
        System.out.println("v1.1.0 (预计 2026-02-01)");
        System.out.println("  - MINOR: 新功能");
        System.out.println("  - 新增：任务搜索功能");
        System.out.println("  - 新增：任务过滤（按状态、按日期）");
        System.out.println("  - 风险：中，可直接升级");
    }
}
