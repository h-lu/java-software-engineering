/*
 * 示例：CampusFlow 中策略模式的应用。
 * 本例演示：任务优先级计算策略（UrgentPriorityStrategy, NormalPriorityStrategy, LowPriorityStrategy）。
 * 运行方式：javac 08_campusflow_strategy.java && java CampusFlowStrategyDemo
 * 预期输出：展示 CampusFlow 中策略模式的应用和扩展
 */

import java.util.*;

// 文件：Task.java（任务实体）
class CampusFlowTaskV2 {
    private final String id;
    private final String title;
    private final String description;
    private final String priority; // urgent, high, normal, low
    private final int estimatedHours;
    private final int actualHours;

    public CampusFlowTaskV2(String id, String title, String description,
                            String priority, int estimatedHours, int actualHours) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.estimatedHours = estimatedHours;
        this.actualHours = actualHours;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getPriority() { return priority; }
    public int getEstimatedHours() { return estimatedHours; }
    public int getActualHours() { return actualHours; }
}

// 文件：PriorityScore.java（优先级分数）
class PriorityScore {
    private final int score; // 0-100
    private final String level; // critical, high, medium, low
    private final String reason;

    public PriorityScore(int score, String level, String reason) {
        this.score = score;
        this.level = level;
        this.reason = reason;
    }

    public int getScore() { return score; }
    public String getLevel() { return level; }
    public String getReason() { return reason; }

    @Override
    public String toString() {
        return String.format("PriorityScore[score=%d, level=%s, reason=%s]", score, level, reason);
    }
}

// 文件：PriorityCalculationStrategy.java（策略接口）
// 策略模式：定义计算任务优先级的算法族，让它们可以互相替换
interface PriorityCalculationStrategy {
    // 计算优先级分数
    PriorityScore calculateScore(CampusFlowTaskV2 task);

    // 是否支持该任务
    boolean supports(CampusFlowTaskV2 task);

    // 获取策略名称
    String getStrategyName();
}

// 文件：UrgentPriorityStrategy.java（紧急优先级策略）
// 适用于紧急任务：高基础分 + 时间压力加成
class UrgentPriorityStrategy implements PriorityCalculationStrategy {
    private static final int BASE_SCORE = 90;
    private static final double TIME_PRESSURE_THRESHOLD = 1.5; // 实际/预估 > 1.5 表示超时

    @Override
    public PriorityScore calculateScore(CampusFlowTaskV2 task) {
        int score = BASE_SCORE;
        StringBuilder reason = new StringBuilder("紧急任务基础分90");

        // 时间压力加成
        if (task.getEstimatedHours() > 0) {
            double ratio = (double) task.getActualHours() / task.getEstimatedHours();
            if (ratio > TIME_PRESSURE_THRESHOLD) {
                score = Math.min(100, score + 5);
                reason.append("，超时风险+5");
            }
        }

        // 工作量调整
        if (task.getEstimatedHours() > 40) {
            score += 3;
            reason.append("，大工作量+3");
        }

        return new PriorityScore(score, "critical", reason.toString());
    }

    @Override
    public boolean supports(CampusFlowTaskV2 task) {
        return "urgent".equals(task.getPriority());
    }

    @Override
    public String getStrategyName() {
        return "紧急优先级策略";
    }
}

// 文件：HighPriorityStrategy.java（高优先级策略）
// 适用于高优先级任务
class HighPriorityStrategy implements PriorityCalculationStrategy {
    private static final int BASE_SCORE = 70;

    @Override
    public PriorityScore calculateScore(CampusFlowTaskV2 task) {
        int score = BASE_SCORE;
        StringBuilder reason = new StringBuilder("高优先级基础分70");

        // 进度影响
        if (task.getEstimatedHours() > 0) {
            double progress = (double) task.getActualHours() / task.getEstimatedHours();
            if (progress > 0.8) {
                score += 5;
                reason.append("，即将完成+5");
            } else if (progress < 0.2) {
                score -= 5;
                reason.append("，刚开始-5");
            }
        }

        // 工作量调整
        if (task.getEstimatedHours() > 20) {
            score += 2;
            reason.append("，中等工作量+2");
        }

        return new PriorityScore(score, "high", reason.toString());
    }

    @Override
    public boolean supports(CampusFlowTaskV2 task) {
        return "high".equals(task.getPriority());
    }

    @Override
    public String getStrategyName() {
        return "高优先级策略";
    }
}

// 文件：NormalPriorityStrategy.java（普通优先级策略）
// 适用于普通任务
class NormalPriorityStrategy implements PriorityCalculationStrategy {
    private static final int BASE_SCORE = 50;

    @Override
    public PriorityScore calculateScore(CampusFlowTaskV2 task) {
        int score = BASE_SCORE;
        StringBuilder reason = new StringBuilder("普通优先级基础分50");

        // 进度影响
        if (task.getEstimatedHours() > 0) {
            double progress = (double) task.getActualHours() / task.getEstimatedHours();
            if (progress > 0.9) {
                score += 10;
                reason.append("，即将完成+10");
            }
        }

        // 积压风险
        if (task.getActualHours() > task.getEstimatedHours()) {
            score += 8;
            reason.append("，已超时+8");
        }

        return new PriorityScore(score, "medium", reason.toString());
    }

    @Override
    public boolean supports(CampusFlowTaskV2 task) {
        return "normal".equals(task.getPriority());
    }

    @Override
    public String getStrategyName() {
        return "普通优先级策略";
    }
}

// 文件：LowPriorityStrategy.java（低优先级策略）
// 适用于低优先级任务
class LowPriorityStrategy implements PriorityCalculationStrategy {
    private static final int BASE_SCORE = 30;

    @Override
    public PriorityScore calculateScore(CampusFlowTaskV2 task) {
        int score = BASE_SCORE;
        StringBuilder reason = new StringBuilder("低优先级基础分30");

        // 即使低优先级，如果快完成了也稍微提升
        if (task.getEstimatedHours() > 0) {
            double progress = (double) task.getActualHours() / task.getEstimatedHours();
            if (progress > 0.95) {
                score += 10;
                reason.append("，即将完成+10");
            }
        }

        // 如果已经超时很久，需要关注
        if (task.getActualHours() > task.getEstimatedHours() * 2) {
            score += 15;
            reason.append("，严重超时+15");
        }

        return new PriorityScore(score, "low", reason.toString());
    }

    @Override
    public boolean supports(CampusFlowTaskV2 task) {
        return "low".equals(task.getPriority());
    }

    @Override
    public String getStrategyName() {
        return "低优先级策略";
    }
}

// 文件：StudentTaskStrategy.java（学生任务策略 - 演示扩展性）
// 适用于学生提交的任务，有特殊的评分规则
class StudentTaskStrategy implements PriorityCalculationStrategy {
    private static final int BASE_SCORE = 60;

    @Override
    public PriorityScore calculateScore(CampusFlowTaskV2 task) {
        int score = BASE_SCORE;
        StringBuilder reason = new StringBuilder("学生任务基础分60");

        // 学生任务考虑学习效率
        if (task.getActualHours() > task.getEstimatedHours()) {
            // 学生可能需要更多时间学习，惩罚较小
            score += 3;
            reason.append("，学习耗时+3");
        }

        // 小任务优先（适合学生碎片时间）
        if (task.getEstimatedHours() <= 4) {
            score += 5;
            reason.append("，小任务适合碎片时间+5");
        }

        return new PriorityScore(score, "medium", reason.toString());
    }

    @Override
    public boolean supports(CampusFlowTaskV2 task) {
        // 假设任务标题包含 "[学生]" 标记
        return task.getTitle().contains("[学生]");
    }

    @Override
    public String getStrategyName() {
        return "学生任务策略";
    }
}

// 文件：PriorityCalculator.java（优先级计算器 - 策略上下文）
class PriorityCalculator {
    private final List<PriorityCalculationStrategy> strategies;

    public PriorityCalculator(List<PriorityCalculationStrategy> strategies) {
        this.strategies = strategies;
    }

    public PriorityScore calculate(CampusFlowTaskV2 task) {
        // 找到第一个支持该任务的策略
        return strategies.stream()
            .filter(s -> s.supports(task))
            .findFirst()
            .map(s -> {
                System.out.println("  使用策略: " + s.getStrategyName());
                return s.calculateScore(task);
            })
            .orElseThrow(() -> new IllegalStateException("没有策略可以处理该任务: " + task.getPriority()));
    }

    // 批量计算并排序
    public List<CampusFlowTaskV2> sortByPriority(List<CampusFlowTaskV2> tasks) {
        return tasks.stream()
            .sorted((t1, t2) -> {
                int score1 = calculate(t1).getScore();
                int score2 = calculate(t2).getScore();
                return Integer.compare(score2, score1); // 降序
            })
            .toList();
    }
}

// 文件：PriorityStrategyFactory.java（策略工厂）
class PriorityStrategyFactory {
    public static List<PriorityCalculationStrategy> createDefaultStrategies() {
        return List.of(
            new StudentTaskStrategy(),  // 先检查学生任务
            new UrgentPriorityStrategy(),
            new HighPriorityStrategy(),
            new NormalPriorityStrategy(),
            new LowPriorityStrategy()
        );
    }

    public static List<PriorityCalculationStrategy> createStrategiesWithoutStudent() {
        return List.of(
            new UrgentPriorityStrategy(),
            new HighPriorityStrategy(),
            new NormalPriorityStrategy(),
            new LowPriorityStrategy()
        );
    }
}

// 文件：CampusFlowStrategyDemo.java（演示入口）
class CampusFlowStrategyDemo {
    public static void main(String[] args) {
        System.out.println("=== CampusFlow 策略模式：任务优先级计算 ===\n");

        // 创建策略列表
        List<PriorityCalculationStrategy> strategies = PriorityStrategyFactory.createDefaultStrategies();
        PriorityCalculator calculator = new PriorityCalculator(strategies);

        // 创建示例任务
        List<CampusFlowTaskV2> tasks = List.of(
            new CampusFlowTaskV2("TASK-001", "修复生产环境Bug", "紧急修复", "urgent", 4, 3),
            new CampusFlowTaskV2("TASK-002", "编写Week 09文档", "文档工作", "normal", 8, 7),
            new CampusFlowTaskV2("TASK-003", "优化数据库查询", "性能优化", "high", 16, 4),
            new CampusFlowTaskV2("TASK-004", "整理代码注释", "代码清理", "low", 2, 0),
            new CampusFlowTaskV2("TASK-005", "[学生]完成Java练习", "学生作业", "normal", 4, 5),
            new CampusFlowTaskV2("TASK-006", "部署新版本", "发布工作", "urgent", 6, 10) // 已超时
        );

        System.out.println("--- 逐个计算优先级 ---");
        for (CampusFlowTaskV2 task : tasks) {
            System.out.println("\n任务: " + task.getTitle());
            System.out.println("  原始优先级: " + task.getPriority());
            System.out.println("  预估工时: " + task.getEstimatedHours() + "h");
            System.out.println("  实际工时: " + task.getActualHours() + "h");
            PriorityScore score = calculator.calculate(task);
            System.out.println("  计算得分: " + score.getScore());
            System.out.println("  优先级级别: " + score.getLevel());
            System.out.println("  原因: " + score.getReason());
        }

        System.out.println("\n\n--- 按优先级排序 ---");
        List<CampusFlowTaskV2> sortedTasks = calculator.sortByPriority(tasks);
        System.out.println("排序结果（从高到低）：");
        for (int i = 0; i < sortedTasks.size(); i++) {
            CampusFlowTaskV2 task = sortedTasks.get(i);
            PriorityScore score = calculator.calculate(task);
            System.out.printf("%d. [%s] %s (得分: %d)%n",
                i + 1, score.getLevel(), task.getTitle(), score.getScore());
        }

        System.out.println("\n\n--- 策略模式的优势 ---");
        System.out.println("1. 消除复杂的 if-else 或 switch 语句");
        System.out.println("2. 每个策略独立，易于理解和测试");
        System.out.println("3. 添加新策略只需实现接口，不修改现有代码");
        System.out.println("4. 策略可以组合和排序（如学生策略优先检查）");
        System.out.println("5. 运行时动态选择策略");

        System.out.println("\n--- 扩展演示：添加新策略 ---");
        System.out.println("假设需要添加'期末周策略'，只需：");
        System.out.println("1. 创建 ExamWeekStrategy 类实现 PriorityCalculationStrategy");
        System.out.println("2. 在工厂方法中添加新策略");
        System.out.println("3. 无需修改 PriorityCalculator 或其他策略");
        System.out.println("这遵循了开闭原则：对扩展开放，对修改关闭");
    }
}
