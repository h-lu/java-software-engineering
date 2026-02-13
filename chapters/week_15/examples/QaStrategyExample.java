/**
 * Week 15: 问答准备示例
 *
 * 演示如何使用 QaStrategy 类来准备演示问答
 */
public class QaStrategyExample {

    public static void main(String[] args) {
        QaStrategy strategy = new QaStrategy();

        // 技术问题
        strategy.addQaPair("为什么用 Java？", "Java 的静态类型系统更适合学习工程化开发。");
        strategy.addQaPair("为什么不用 Spring？", "Spring 框架太重，不适合作为学习项目使用。");
        strategy.addQaPair("为什么用 SQLite？", "SQLite 零配置，非常适合小项目快速开发使用。");

        // 设计问题
        strategy.addQaPair("架构设计是什么？", "我们采用领域模型、仓储、控制器分层架构。");
        strategy.addQaPair("模型如何设计？", "核心实体是任务，参考了领域驱动设计思想。");

        // 工程问题
        strategy.addQaPair("测试覆盖率多少？", "我们使用 JUnit 5 编写测试，覆盖率达到百分之八十。");
        strategy.addQaPair("如何部署？", "使用 Railway 自动部署，推送代码后会自动构建。");

        // 未来问题
        strategy.addQaPair("未来计划是什么？", "短期增加搜索功能，长期迁移到 PostgreSQL 数据库。");
        strategy.addQaPair("下一步做什么？", "优化移动端体验，增加数据导出和其他功能。");

        // 补充到 10 个
        strategy.addQaPair("Bug Bash 发现了什么？", "其他组发现了三个 Bug，我们全部都修复完成了。");

        // 验证准备是否完整
        if (strategy.isComplete()) {
            System.out.println("问答准备完整！共 " + strategy.getQaCount() + " 个问答对。");

            // 打印所有问题
            System.out.println("\n准备的问题清单：");
            for (String question : strategy.getAllQuestions()) {
                System.out.println("- " + question);
                System.out.println("  回答: " + strategy.getAnswer(question));
            }
        } else {
            System.out.println("问答准备不完整，请检查：");
            if (!strategy.hasTechnicalQuestions()) {
                System.out.println("- 缺少技术问题");
            }
            if (!strategy.hasDesignQuestions()) {
                System.out.println("- 缺少设计问题");
            }
            if (!strategy.hasPracticeQuestions()) {
                System.out.println("- 缺少工程问题");
            }
            if (!strategy.hasFutureQuestions()) {
                System.out.println("- 缺少未来问题");
            }
            if (!strategy.hasEnoughQaPairs()) {
                System.out.println("- 问答数量不足（需要至少 10 个）");
            }
        }
    }
}
