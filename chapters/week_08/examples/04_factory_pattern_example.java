/*
 * 示例：工厂模式管理策略对象创建。
 * 本例演示：工厂模式（Factory Pattern）如何封装对象创建逻辑。
 * 运行方式：javac 04_factory_pattern_example.java && java FactoryPatternDemo
 * 预期输出：展示工厂如何简化策略对象的创建和管理
 */

import java.util.*;

// 文件：FeeCalculationStrategy.java（策略接口）
interface FeeCalculationStrategyV4 {
    double calculateFee(int daysOverdue);
    boolean supports(String priority);
    String getStrategyName();
}

// 文件：HighPriorityFeeStrategy.java（高优先级策略）
class HighPriorityFeeStrategyV4 implements FeeCalculationStrategyV4 {
    private static final double BASE_FEE = 10.0;
    private static final double PRIORITY_MULTIPLIER = 3.0;

    @Override
    public double calculateFee(int daysOverdue) {
        double multiplier = applyOverdueMultiplier(PRIORITY_MULTIPLIER, daysOverdue);
        return BASE_FEE * daysOverdue * multiplier;
    }

    private double applyOverdueMultiplier(double multiplier, int daysOverdue) {
        if (daysOverdue > 30) {
            return multiplier * 1.5;
        } else if (daysOverdue > 7) {
            return multiplier * 1.2;
        }
        return multiplier;
    }

    @Override
    public boolean supports(String priority) {
        return "high".equals(priority);
    }

    @Override
    public String getStrategyName() {
        return "高优先级费用策略";
    }
}

// 文件：MediumPriorityFeeStrategy.java（中优先级策略）
class MediumPriorityFeeStrategyV4 implements FeeCalculationStrategyV4 {
    private static final double BASE_FEE = 10.0;
    private static final double PRIORITY_MULTIPLIER = 2.0;

    @Override
    public double calculateFee(int daysOverdue) {
        double multiplier = applyOverdueMultiplier(PRIORITY_MULTIPLIER, daysOverdue);
        return BASE_FEE * daysOverdue * multiplier;
    }

    private double applyOverdueMultiplier(double multiplier, int daysOverdue) {
        if (daysOverdue > 30) {
            return multiplier * 1.5;
        } else if (daysOverdue > 7) {
            return multiplier * 1.2;
        }
        return multiplier;
    }

    @Override
    public boolean supports(String priority) {
        return "medium".equals(priority);
    }

    @Override
    public String getStrategyName() {
        return "中优先级费用策略";
    }
}

// 文件：LowPriorityFeeStrategy.java（低优先级策略）
class LowPriorityFeeStrategyV4 implements FeeCalculationStrategyV4 {
    private static final double BASE_FEE = 10.0;
    private static final double PRIORITY_MULTIPLIER = 1.0;

    @Override
    public double calculateFee(int daysOverdue) {
        double multiplier = applyOverdueMultiplier(PRIORITY_MULTIPLIER, daysOverdue);
        return BASE_FEE * daysOverdue * multiplier;
    }

    private double applyOverdueMultiplier(double multiplier, int daysOverdue) {
        if (daysOverdue > 30) {
            return multiplier * 1.5;
        } else if (daysOverdue > 7) {
            return multiplier * 1.2;
        }
        return multiplier;
    }

    @Override
    public boolean supports(String priority) {
        return "low".equals(priority);
    }

    @Override
    public String getStrategyName() {
        return "低优先级费用策略";
    }
}

// 文件：StudentDiscountStrategy.java（学生优惠策略 - 演示扩展性）
class StudentDiscountStrategyV4 implements FeeCalculationStrategyV4 {
    private static final double BASE_FEE = 10.0;
    private static final double DISCOUNT_RATE = 0.5; // 50% 折扣

    @Override
    public double calculateFee(int daysOverdue) {
        double normalFee = BASE_FEE * daysOverdue;
        return normalFee * DISCOUNT_RATE;
    }

    @Override
    public boolean supports(String priority) {
        return "student".equals(priority);
    }

    @Override
    public String getStrategyName() {
        return "学生优惠策略（50%折扣）";
    }
}

// 文件：FeeStrategyFactory.java（工厂类）
// 工厂模式：封装对象创建逻辑，客户端无需知道具体类名
class FeeStrategyFactory {

    // 工厂方法：创建默认策略列表
    public static List<FeeCalculationStrategyV4> createDefaultStrategies() {
        return List.of(
            new HighPriorityFeeStrategyV4(),
            new MediumPriorityFeeStrategyV4(),
            new LowPriorityFeeStrategyV4()
        );
    }

    // 工厂方法：创建包含学生优惠的策略列表
    public static List<FeeCalculationStrategyV4> createStrategiesWithStudentDiscount() {
        return List.of(
            new HighPriorityFeeStrategyV4(),
            new MediumPriorityFeeStrategyV4(),
            new LowPriorityFeeStrategyV4(),
            new StudentDiscountStrategyV4()
        );
    }

    // 工厂方法：根据优先级创建单个策略
    public static FeeCalculationStrategyV4 createStrategy(String priority) {
        return switch (priority) {
            case "high" -> new HighPriorityFeeStrategyV4();
            case "medium" -> new MediumPriorityFeeStrategyV4();
            case "low" -> new LowPriorityFeeStrategyV4();
            case "student" -> new StudentDiscountStrategyV4();
            default -> throw new IllegalArgumentException("不支持的优先级: " + priority);
        };
    }

    // 工厂方法：根据配置创建策略列表
    public static List<FeeCalculationStrategyV4> createStrategiesFromConfig(boolean includeStudentDiscount) {
        List<FeeCalculationStrategyV4> strategies = new ArrayList<>();
        strategies.add(new HighPriorityFeeStrategyV4());
        strategies.add(new MediumPriorityFeeStrategyV4());
        strategies.add(new LowPriorityFeeStrategyV4());

        if (includeStudentDiscount) {
            strategies.add(new StudentDiscountStrategyV4());
        }

        return strategies;
    }
}

// 文件：FeeCalculator.java（使用策略的客户端）
class FeeCalculatorV4 {
    private final List<FeeCalculationStrategyV4> strategies;

    public FeeCalculatorV4(List<FeeCalculationStrategyV4> strategies) {
        this.strategies = strategies;
    }

    public double calculateFee(String priority, int daysOverdue) {
        FeeCalculationStrategyV4 strategy = strategies.stream()
            .filter(s -> s.supports(priority))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("不支持的优先级: " + priority));

        double fee = strategy.calculateFee(daysOverdue);
        System.out.println("  使用策略: " + strategy.getStrategyName());
        System.out.println("  逾期天数: " + daysOverdue);
        System.out.println("  计算费用: ¥" + String.format("%.2f", fee));

        return fee;
    }
}

// 文件：FactoryPatternDemo.java（演示入口）
class FactoryPatternDemo {
    public static void main(String[] args) {
        System.out.println("=== 工厂模式：策略对象创建演示 ===\n");

        // 演示 1：使用默认策略
        System.out.println("--- 场景 1：默认策略 ---");
        List<FeeCalculationStrategyV4> defaultStrategies = FeeStrategyFactory.createDefaultStrategies();
        FeeCalculatorV4 calculator1 = new FeeCalculatorV4(defaultStrategies);

        System.out.println("高优先级任务逾期 10 天:");
        calculator1.calculateFee("high", 10);
        System.out.println();

        System.out.println("低优先级任务逾期 5 天:");
        calculator1.calculateFee("low", 5);
        System.out.println();

        // 演示 2：使用工厂创建单个策略
        System.out.println("--- 场景 2：创建单个策略 ---");
        FeeCalculationStrategyV4 mediumStrategy = FeeStrategyFactory.createStrategy("medium");
        System.out.println("创建策略: " + mediumStrategy.getStrategyName());
        System.out.println("逾期 15 天费用: ¥" + String.format("%.2f", mediumStrategy.calculateFee(15)));
        System.out.println();

        // 演示 3：使用包含学生优惠的策略
        System.out.println("--- 场景 3：包含学生优惠的策略 ---");
        List<FeeCalculationStrategyV4> extendedStrategies = FeeStrategyFactory.createStrategiesWithStudentDiscount();
        FeeCalculatorV4 calculator2 = new FeeCalculatorV4(extendedStrategies);

        System.out.println("学生任务逾期 10 天:");
        calculator2.calculateFee("student", 10);
        System.out.println();

        // 演示 4：根据配置动态创建
        System.out.println("--- 场景 4：根据配置动态创建 ---");
        boolean isStudentMode = true;
        List<FeeCalculationStrategyV4> configStrategies =
            FeeStrategyFactory.createStrategiesFromConfig(isStudentMode);
        FeeCalculatorV4 calculator3 = new FeeCalculatorV4(configStrategies);

        System.out.println("配置模式: " + (isStudentMode ? "包含学生优惠" : "标准模式"));
        System.out.println("策略数量: " + configStrategies.size());
        System.out.println("可用策略:");
        configStrategies.forEach(s -> System.out.println("  - " + s.getStrategyName()));
        System.out.println();

        // 演示 5：对比工厂前后的代码
        System.out.println("--- 工厂模式的优势 ---");
        System.out.println("使用前（直接创建）:");
        System.out.println("  List<Strategy> strategies = List.of(");
        System.out.println("      new HighPriorityFeeStrategy(),");
        System.out.println("      new MediumPriorityFeeStrategy(),");
        System.out.println("      new LowPriorityFeeStrategy()");
        System.out.println("  );");
        System.out.println();
        System.out.println("使用后（工厂创建）:");
        System.out.println("  List<Strategy> strategies = FeeStrategyFactory.createDefaultStrategies();");
        System.out.println();

        System.out.println("=== 工厂模式的优势 ===");
        System.out.println("1. 封装创建逻辑：客户端无需知道具体类名");
        System.out.println("2. 集中管理：所有创建逻辑在一个地方");
        System.out.println("3. 易于扩展：添加新策略只需修改工厂");
        System.out.println("4. 配置化：可以根据配置动态创建不同的策略组合");
        System.out.println("5. 降低耦合：客户端依赖工厂，不依赖具体策略类");
    }
}
