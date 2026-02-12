/*
 * 示例：圈复杂度计算示例。
 * 本例演示：圈复杂度（Cyclomatic Complexity）的概念和计算方法。
 * 运行方式：javac 05_code_metrics_example.java && java CodeMetricsDemo
 * 预期输出：展示不同代码的圈复杂度对比
 */

/**
 * 圈复杂度（Cyclomatic Complexity）是衡量代码复杂度的指标。
 * 计算公式：M = E - N + 2P
 * 简化计算：M = 1 + 条件语句数量（if, while, for, case, catch, &&, || 等）
 *
 * 业界建议：
 * - 1-10：简单，风险低
 * - 11-20：较复杂，中等风险
 * - 21-50：复杂，高风险
 * - 50+：非常复杂，极高风险
 */

// 文件：CodeMetricsBefore.java（高圈复杂度版本）
// 圈复杂度 = 1（基础）+ 9（条件语句）= 10
class CodeMetricsBefore {

    /**
     * 圈复杂度 = 1 + 9 = 10（过高）
     * 条件语句：
     * 1. if (score < 0)
     * 2. if (score > 100)
     * 3. if (score >= 90)
     * 4. else if (score >= 80)
     * 5. else if (score >= 70)
     * 6. else if (score >= 60)
     * 7. if (attendance < 80)
     * 8. if (bonus)
     * 9. if (score >= 60 && score < 70)
     */
    public String calculateGrade(int score, int attendance, boolean bonus) {
        // 输入验证
        if (score < 0) {
            throw new IllegalArgumentException("分数不能为负数");
        }
        if (score > 100) {
            throw new IllegalArgumentException("分数不能超过100");
        }

        String grade;

        // 成绩判定
        if (score >= 90) {
            grade = "A";
        } else if (score >= 80) {
            grade = "B";
        } else if (score >= 70) {
            grade = "C";
        } else if (score >= 60) {
            grade = "D";
        } else {
            grade = "F";
        }

        // 出勤率调整
        if (attendance < 80) {
            if (score >= 60 && score < 70) {
                grade = "F"; // 出勤率低，降一级
            }
        }

        // 额外加分
        if (bonus) {
            if (grade.equals("B")) grade = "A";
            if (grade.equals("C")) grade = "B";
        }

        return grade;
    }

    /**
     * 圈复杂度 = 1 + 12 = 13（严重超标）
     * 这是一个典型的需要重构的方法
     */
    public double calculateShippingFee(String region, double weight, boolean isExpress,
                                       boolean isMember, String couponCode) {
        double baseFee = 0;

        // 地区判断
        if (region.equals("local")) {
            baseFee = 10;
        } else if (region.equals("domestic")) {
            baseFee = 20;
        } else if (region.equals("international")) {
            baseFee = 50;
        } else {
            throw new IllegalArgumentException("未知地区");
        }

        // 重量计算
        if (weight <= 1) {
            // 基础费用
        } else if (weight <= 5) {
            baseFee += (weight - 1) * 5;
        } else if (weight <= 10) {
            baseFee += 4 * 5 + (weight - 5) * 8;
        } else {
            baseFee += 4 * 5 + 5 * 8 + (weight - 10) * 12;
        }

        // 快递加急
        if (isExpress) {
            baseFee *= 1.5;
        }

        // 会员折扣
        if (isMember) {
            baseFee *= 0.9;
        }

        // 优惠券
        if (couponCode != null && !couponCode.isEmpty()) {
            if (couponCode.startsWith("VIP")) {
                baseFee -= 20;
            } else if (couponCode.startsWith("NEW")) {
                baseFee -= 10;
            }
        }

        return Math.max(0, baseFee);
    }
}

// 文件：CodeMetricsAfter.java（低圈复杂度版本 - 重构后）
class CodeMetricsAfter {

    /**
     * 圈复杂度 = 1 + 3 = 4（良好）
     * 重构策略：提取方法 + 使用 Map 替代条件判断
     */
    public String calculateGrade(int score, int attendance, boolean bonus) {
        validateScore(score);
        String grade = determineBaseGrade(score);
        grade = applyAttendancePenalty(grade, score, attendance);
        grade = applyBonus(grade, bonus);
        return grade;
    }

    // 圈复杂度 = 1 + 2 = 3
    private void validateScore(int score) {
        if (score < 0) {
            throw new IllegalArgumentException("分数不能为负数");
        }
        if (score > 100) {
            throw new IllegalArgumentException("分数不能超过100");
        }
    }

    // 圈复杂度 = 1 + 1 = 2（使用 switch 表达式）
    private String determineBaseGrade(int score) {
        return switch (score / 10) {
            case 10, 9 -> "A";
            case 8 -> "B";
            case 7 -> "C";
            case 6 -> "D";
            default -> "F";
        };
    }

    // 圈复杂度 = 1 + 1 = 2
    private String applyAttendancePenalty(String grade, int score, int attendance) {
        if (attendance < 80 && score >= 60 && score < 70) {
            return "F";
        }
        return grade;
    }

    // 圈复杂度 = 1 + 1 = 2
    private String applyBonus(String grade, boolean bonus) {
        if (!bonus) return grade;
        return switch (grade) {
            case "B" -> "A";
            case "C" -> "B";
            default -> grade;
        };
    }
}

// 文件：ShippingFeeCalculator.java（重构后的运费计算）
// 使用策略模式消除复杂条件判断
interface RegionStrategy {
    double calculateBaseFee(double weight);
    boolean supports(String region);

    // 默认方法：计算重量费用
    default double calculateWeightFee(double weight, double tier1Rate,
                                       double tier2Rate, double tier3Rate) {
        if (weight <= 1) return 0;
        if (weight <= 5) return (weight - 1) * tier1Rate;
        if (weight <= 10) return 4 * tier1Rate + (weight - 5) * tier2Rate;
        return 4 * tier1Rate + 5 * tier2Rate + (weight - 10) * tier3Rate;
    }
}

class LocalRegionStrategy implements RegionStrategy {
    public boolean supports(String region) { return "local".equals(region); }
    public double calculateBaseFee(double weight) {
        return 10 + calculateWeightFee(weight, 5, 8, 12);
    }
}

class DomesticRegionStrategy implements RegionStrategy {
    public boolean supports(String region) { return "domestic".equals(region); }
    public double calculateBaseFee(double weight) {
        return 20 + calculateWeightFee(weight, 5, 8, 12);
    }
}

class InternationalRegionStrategy implements RegionStrategy {
    public boolean supports(String region) { return "international".equals(region); }
    public double calculateBaseFee(double weight) {
        return 50 + calculateWeightFee(weight, 10, 15, 20);
    }
}

class ShippingFeeCalculator {
    private final java.util.List<RegionStrategy> regionStrategies;

    public ShippingFeeCalculator() {
        this.regionStrategies = java.util.List.of(
            new LocalRegionStrategy(),
            new DomesticRegionStrategy(),
            new InternationalRegionStrategy()
        );
    }

    // 圈复杂度 = 1 + 3 = 4（大幅降低）
    public double calculateShippingFee(String region, double weight, boolean isExpress,
                                       boolean isMember, String couponCode) {
        double fee = calculateBaseFee(region, weight);
        fee = applyExpressFee(fee, isExpress);
        fee = applyMemberDiscount(fee, isMember);
        fee = applyCoupon(fee, couponCode);
        return Math.max(0, fee);
    }

    // 圈复杂度 = 1 + 1 = 2
    private double calculateBaseFee(String region, double weight) {
        return regionStrategies.stream()
            .filter(s -> s.supports(region))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("未知地区"))
            .calculateBaseFee(weight);
    }

    // 圈复杂度 = 1 + 1 = 2
    private double applyExpressFee(double fee, boolean isExpress) {
        return isExpress ? fee * 1.5 : fee;
    }

    // 圈复杂度 = 1 + 1 = 2
    private double applyMemberDiscount(double fee, boolean isMember) {
        return isMember ? fee * 0.9 : fee;
    }

    // 圈复杂度 = 1 + 2 = 3
    private double applyCoupon(double fee, String couponCode) {
        if (couponCode == null || couponCode.isEmpty()) return fee;
        if (couponCode.startsWith("VIP")) return fee - 20;
        if (couponCode.startsWith("NEW")) return fee - 10;
        return fee;
    }

}

// 文件：CodeMetricsDemo.java（演示入口）
class CodeMetricsDemo {
    public static void main(String[] args) {
        System.out.println("=== 圈复杂度（Cyclomatic Complexity）演示 ===\n");

        System.out.println("圈复杂度计算公式：M = 1 + 条件语句数量");
        System.out.println("条件语句包括：if, while, for, case, catch, &&, || 等\n");

        System.out.println("--- 重构前：高圈复杂度 ---");
        System.out.println("calculateGrade 方法：");
        System.out.println("  圈复杂度 = 1（基础）+ 9（条件语句）= 10");
        System.out.println("  条件语句：");
        System.out.println("    1. if (score < 0)");
        System.out.println("    2. if (score > 100)");
        System.out.println("    3. if (score >= 90)");
        System.out.println("    4. else if (score >= 80)");
        System.out.println("    5. else if (score >= 70)");
        System.out.println("    6. else if (score >= 60)");
        System.out.println("    7. if (attendance < 80)");
        System.out.println("    8. if (bonus)");
        System.out.println("    9. if (score >= 60 && score < 70)");
        System.out.println("  评级: ⚠️ 较复杂，需要重构\n");

        System.out.println("calculateShippingFee 方法：");
        System.out.println("  圈复杂度 = 1 + 12 = 13");
        System.out.println("  评级: ⚠️ 严重超标，必须重构\n");

        System.out.println("--- 重构后：低圈复杂度 ---");
        System.out.println("calculateGrade 方法：");
        System.out.println("  主方法圈复杂度 = 1 + 3 = 4");
        System.out.println("  子方法圈复杂度：");
        System.out.println("    - validateScore: 3");
        System.out.println("    - determineBaseGrade: 2");
        System.out.println("    - applyAttendancePenalty: 2");
        System.out.println("    - applyBonus: 2");
        System.out.println("  评级: ✅ 良好\n");

        System.out.println("ShippingFeeCalculator.calculateShippingFee：");
        System.out.println("  圈复杂度 = 1 + 3 = 4");
        System.out.println("  评级: ✅ 良好\n");

        System.out.println("--- 业界标准 ---");
        System.out.println("圈复杂度范围    风险等级    建议措施");
        System.out.println("1-10           低         ✅ 可接受");
        System.out.println("11-20          中         ⚠️ 考虑重构");
        System.out.println("21-50          高         ❌ 需要重构");
        System.out.println("50+            极高       ❌ 必须立即重构\n");

        System.out.println("--- 降低圈复杂度的技巧 ---");
        System.out.println("1. 提取方法：将复杂逻辑拆分成小方法");
        System.out.println("2. 使用 switch 表达式替代 if-else 链");
        System.out.println("3. 使用策略模式消除复杂条件判断");
        System.out.println("4. 使用查找表（Map）替代条件判断");
        System.out.println("5. 卫语句（Guard Clauses）减少嵌套\n");

        // 功能演示
        System.out.println("--- 功能验证 ---");
        CodeMetricsAfter calculator = new CodeMetricsAfter();

        System.out.println("成绩 85, 出勤 90, 无加分 -> " + calculator.calculateGrade(85, 90, false));
        System.out.println("成绩 65, 出勤 70, 无加分 -> " + calculator.calculateGrade(65, 70, false) + " (出勤率影响)");
        System.out.println("成绩 85, 出勤 90, 有加分 -> " + calculator.calculateGrade(85, 90, true) + " (加分提升)");
    }
}
