package com.campusflow;

import java.util.List;

/**
 * CampusFlow 重构示例应用程序
 *
 * <p>Week 08: 设计模式与代码重构
 * 演示策略模式、工厂模式在费用计算中的应用
 */
public class App {

    public static void main(String[] args) {
        System.out.println("=== CampusFlow 重构示例 ===\n");

        // 使用工厂创建策略
        List<FeeCalculationStrategy> strategies = FeeStrategyFactory.createDefaultStrategies();
        System.out.println("已创建 " + strategies.size() + " 种费用计算策略");

        // 创建内存仓库（演示用）
        InMemoryTaskRepository repository = new InMemoryTaskRepository();

        // 创建服务
        TaskService service = new TaskService(repository, strategies);
        System.out.println("TaskService 初始化完成\n");

        // 创建不同优先级的任务
        Task highPriorityTask = service.createTask("完成Week08作业", "编写重构测试", "high");
        Task mediumPriorityTask = service.createTask("整理笔记", "整理Week08学习笔记", "medium");
        Task lowPriorityTask = service.createTask("阅读参考资料", "阅读设计模式相关文章", "low");

        System.out.println("已创建任务:");
        System.out.println("  - " + highPriorityTask.getTitle() + " (优先级: " + highPriorityTask.getPriority() + ")");
        System.out.println("  - " + mediumPriorityTask.getTitle() + " (优先级: " + mediumPriorityTask.getPriority() + ")");
        System.out.println("  - " + lowPriorityTask.getTitle() + " (优先级: " + lowPriorityTask.getPriority() + ")");
        System.out.println();

        // 计算各任务的逾期费用（假设逾期10天）
        int daysOverdue = 10;
        System.out.println("假设逾期 " + daysOverdue + " 天，计算逾期费用:");

        double highFee = service.calculateOverdueFee(highPriorityTask.getId(), daysOverdue);
        double mediumFee = service.calculateOverdueFee(mediumPriorityTask.getId(), daysOverdue);
        double lowFee = service.calculateOverdueFee(lowPriorityTask.getId(), daysOverdue);

        System.out.printf("  - 高优先级任务逾期费用: %.2f 元%n", highFee);
        System.out.printf("  - 中优先级任务逾期费用: %.2f 元%n", mediumFee);
        System.out.printf("  - 低优先级任务逾期费用: %.2f 元%n", lowFee);
        System.out.println();

        // 演示重构前后的对比
        System.out.println("=== 重构前后对比 ===");
        OverdueFeeCalculator calculator = new OverdueFeeCalculator();

        String[] priorities = {"high", "medium", "low"};
        for (String priority : priorities) {
            double legacy = calculator.calculateFeeLegacy(priority, daysOverdue);
            double refactored = calculator.calculateFeeRefactored(priority, daysOverdue);
            System.out.printf("%s 优先级 (%d天): 重构前=%.2f, 重构后=%.2f, 一致=%s%n",
                priority, daysOverdue, legacy, refactored, (legacy == refactored ? "是" : "否"));
        }

        System.out.println("\n=== 完成 ===");
        System.out.println("运行 'mvn test' 查看完整测试套件（91个测试用例）");
    }
}
