/*
 * 示例：能力雷达图生成器
 * 运行方式：javac SkillRadarGenerator.java && java SkillRadarGenerator
 * 预期输出：生成 skill_radar.md，包含 ASCII 雷达图和详细评估
 *
 * Week 16: 项目展示与工程复盘
 * 本例演示如何用程序生成能力评估报告和雷达图
 */

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 能力维度
 */
class SkillDimension {
    private final String name;
    private final String nameEn;
    private int score; // 1-5 分
    private final int initialScore;
    private final List<String> evidences; // 证据

    public SkillDimension(String name, String nameEn, int initialScore) {
        this.name = name;
        this.nameEn = nameEn;
        this.initialScore = initialScore;
        this.score = initialScore;
        this.evidences = new ArrayList<>();
    }

    public void setScore(int score) {
        if (score < 1 || score > 5) {
            throw new IllegalArgumentException("分数必须在 1-5 之间");
        }
        this.score = score;
    }

    public void addEvidence(String evidence) {
        evidences.add(evidence);
    }

    public String getName() {
        return name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public int getScore() {
        return score;
    }

    public int getInitialScore() {
        return initialScore;
    }

    public int getImprovement() {
        return score - initialScore;
    }

    public List<String> getEvidences() {
        return new ArrayList<>(evidences);
    }
}

/**
 * 能力评估器
 */
class SkillAssessment {
    private final String userName;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final List<SkillDimension> dimensions;

    public SkillAssessment(String userName, LocalDate startDate, LocalDate endDate) {
        this.userName = userName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.dimensions = new ArrayList<>();
        initializeDimensions();
    }

    private void initializeDimensions() {
        dimensions.add(new SkillDimension("编程语言", "Programming Language", 2));
        dimensions.add(new SkillDimension("工程工具", "Engineering Tools", 2));
        dimensions.add(new SkillDimension("测试能力", "Testing", 1));
        dimensions.add(new SkillDimension("架构设计", "Architecture Design", 1));
        dimensions.add(new SkillDimension("文档能力", "Documentation", 1));
        dimensions.add(new SkillDimension("团队协作", "Team Collaboration", 1));
    }

    public SkillDimension getDimension(String name) {
        return dimensions.stream()
            .filter(d -> d.getName().equals(name))
            .findFirst()
            .orElse(null);
    }

    public double getAverageScore() {
        return dimensions.stream()
            .mapToInt(SkillDimension::getScore)
            .average()
            .orElse(0);
    }

    public double getInitialAverageScore() {
        return dimensions.stream()
            .mapToInt(SkillDimension::getInitialScore)
            .average()
            .orElse(0);
    }

    /**
     * 生成 ASCII 雷达图
     */
    public String generateRadarChart() {
        StringBuilder chart = new StringBuilder();
        chart.append("```\n");
        chart.append("       ").append(dimensions.get(0).getName()).append("\n");
        chart.append("        ").append(renderBar(dimensions.get(0).getScore())).append("\n");
        chart.append("         /      \\\n");
        chart.append(dimensions.get(4).getName()).append("  ");
        chart.append(renderBar(dimensions.get(4).getScore())).append("    ");
        chart.append(renderBar(dimensions.get(1).getScore())).append("  ");
        chart.append(dimensions.get(1).getName()).append("\n");
        chart.append("       |      |\n");
        chart.append(dimensions.get(5).getName()).append("  ");
        chart.append(renderBar(dimensions.get(5).getScore())).append("    ");
        chart.append(renderBar(dimensions.get(3).getScore())).append("  ");
        chart.append(dimensions.get(3).getName()).append("\n");
        chart.append("         \\      /\n");
        chart.append("          ").append(renderBar(dimensions.get(2).getScore())).append("\n");
        chart.append("       ").append(dimensions.get(2).getName()).append("\n");
        chart.append("```\n");
        return chart.toString();
    }

    private String renderBar(int score) {
        StringBuilder bar = new StringBuilder("●");
        for (int i = 1; i < score; i++) {
            bar.append("—");
        }
        return bar.toString();
    }

    /**
     * 评估等级
     */
    public String getLevel() {
        double avg = getAverageScore();
        if (avg < 2) return "初级工程师";
        if (avg < 3.5) return "接近中级工程师";
        if (avg < 4.5) return "中级工程师";
        return "高级工程师";
    }

    /**
     * 导出为 Markdown 报告
     */
    public void exportToMarkdown(String filename) throws IOException {
        StringBuilder report = new StringBuilder();

        // 标题
        report.append("# ").append(userName).append(" 的能力成长评估\n\n");
        report.append("评估周期: ").append(startDate).append(" 至 ").append(endDate).append("\n");
        Period period = Period.between(startDate, endDate);
        report.append("时长: ").append(period.getMonths()).append(" 个月\n\n");
        report.append("---\n\n");

        // 雷达图
        report.append("## 能力雷达图\n\n");
        report.append(generateRadarChart());

        // 统计
        report.append("**平均分**: ").append(String.format("%.1f", getInitialAverageScore()))
            .append(" → ").append(String.format("%.1f", getAverageScore()))
            .append(" (提升 ").append(String.format("%.0f%%",
                (getAverageScore() - getInitialAverageScore()) / getInitialAverageScore() * 100))
            .append(")\n\n");
        report.append("**当前等级**: ").append(getLevel()).append("\n\n");

        // 各维度详细评估
        report.append("## 各维度详细评估\n\n");

        for (SkillDimension dim : dimensions) {
            report.append("### ").append(dim.getName()).append(" (").append(dim.getNameEn()).append(")\n\n");
            report.append("**评分**: ").append(dim.getInitialScore()).append(" → ")
                .append(dim.getScore()).append(" (提升 ").append(dim.getImprovement()).append(")\n\n");

            if (!dim.getEvidences().isEmpty()) {
                report.append("**证据**:\n");
                for (String evidence : dim.getEvidences()) {
                    report.append("- ").append(evidence).append("\n");
                }
                report.append("\n");
            }

            if (dim.getScore() < 4) {
                report.append("**下一步**: \n");
                report.append(getNextSteps(dim.getName())).append("\n\n");
            }

            report.append("---\n\n");
        }

        // 成长建议
        report.append("## 成长建议\n\n");
        report.append(generateRecommendations());

        Files.write(Paths.get(filename), report.toString().getBytes());
        System.out.println("报告已导出到: " + filename);
    }

    private String getNextSteps(String dimensionName) {
        switch (dimensionName) {
            case "编程语言":
                return "- 学习 Java 8 新特性（Optional、Stream）\n- 研究 Spring Boot 的依赖注入原理";
            case "工程工具":
                return "- 学习 GitHub Actions，配置自动化 CI/CD\n- 尝试 Docker 容器化部署";
            case "测试能力":
                return "- 学习 Testcontainers，做真数据库集成测试\n- 研究测试金字塔";
            case "架构设计":
                return "- 学习 DDD（领域驱动设计）\n- 研究微服务架构 vs 单体应用";
            case "文档能力":
                return "- 学习文档审查检查清单\n- 写一篇技术博客";
            case "团队协作":
                return "- 学习如何带领 3-5 人小团队\n- 练习如何拒绝不合理需求";
            default:
                return "- 持续学习";
        }
    }

    private String generateRecommendations() {
        StringBuilder recs = new StringBuilder();

        // 强项
        recs.append("### 强项（Keep）\n\n");
        dimensions.stream()
            .filter(d -> d.getScore() >= 4)
            .forEach(d -> recs.append("- **").append(d.getName()).append("**: ")
                .append(d.getEvidences().get(0)).append("\n"));
        recs.append("\n");

        // 弱项
        recs.append("### 弱项（Problem）\n\n");
        dimensions.stream()
            .filter(d -> d.getScore() <= 2)
            .forEach(d -> recs.append("- **").append(d.getName()).append("**: ")
                .append(getNextSteps(d.getName()).replace("\n", "\n  ")).append("\n"));
        recs.append("\n");

        // 下一步
        recs.append("### 下一步行动（Try）\n\n");
        recs.append("1. **巩固基础**（1 个月）:\n");
        recs.append("   - 重读《代码整洁之道》，每周重构 1 个类\n");
        recs.append("   - 学习 Java 8 新特性\n\n");

        recs.append("2. **扩展工具链**（1 个月）:\n");
        recs.append("   - 学习 Docker\n");
        recs.append("   - 配置 GitHub Actions\n\n");

        recs.append("3. **深入架构**（1 个月）:\n");
        recs.append("   - 学习 Spring Boot\n");
        recs.append("   - 研究 MySQL\n\n");

        return recs.toString();
    }
}

/**
 * 主程序：生成 CampusFlow 团队能力评估
 */
public class SkillRadarGenerator {
    public static void main(String[] args) throws IOException {
        // 创建评估：小北，从 2025-09-01 到 2026-01-13（约 16 周）
        SkillAssessment assessment = new SkillAssessment(
            "小北",
            LocalDate.of(2025, 9, 1),
            LocalDate.of(2026, 1, 13)
        );

        // 设置各维度分数和证据
        SkillDimension programming = assessment.getDimension("编程语言");
        programming.setScore(4);
        programming.addEvidence("熟悉 Java 核心 API，能做技术选型");
        programming.addEvidence("CampusFlow 的 BookingService 有 15 个公开方法，API 设计清晰");
        programming.addEvidence("Week 08 用策略模式重构预约规则");

        SkillDimension tools = assessment.getDimension("工程工具");
        tools.setScore(4);
        tools.addEvidence("熟练 Feature Branch 工作流，能解决合并冲突");
        tools.addEvidence("Week 14 用 Maven Shade Plugin 打包");
        tools.addEvidence("Week 11 配置 JaCoCo，覆盖率检查");

        SkillDimension testing = assessment.getDimension("测试能力");
        testing.setScore(4);
        testing.addEvidence("能写 JUnit 5 测试：@Test、@BeforeEach、@ParameterizedTest");
        testing.addEvidence("测试覆盖率 87%，测试用例 127 个");
        testing.addEvidence("Week 12 Bug Bash 时，有测试的模块 0 bug");

        SkillDimension architecture = assessment.getDimension("架构设计");
        architecture.setScore(3);
        architecture.addEvidence("能用名词提取法做领域建模");
        architecture.addEvidence("写了 4 篇 ADR，记录架构决策");
        architecture.addEvidence("CampusFlow 分层：Controller → Service → Repository");
        architecture.addEvidence("不足：Booking 类有点臃肿（15 个方法）");

        SkillDimension documentation = assessment.getDimension("文档能力");
        documentation.setScore(3);
        documentation.addEvidence("能写结构清晰的 README");
        documentation.addEvidence("能写 ADR，4 篇共 3000 字");
        documentation.addEvidence("Week 13 用 Swagger UI 自动生成交互式文档");
        documentation.addEvidence("不足：没写过技术博客");

        SkillDimension collaboration = assessment.getDimension("团队协作");
        collaboration.setScore(3);
        collaboration.addEvidence("能参与 Code Review，给出建设性反馈");
        collaboration.addEvidence("当过 2 轮首席架构师");
        collaboration.addEvidence("Week 15 演示时，和队友配合，Q&A 互相补充");
        collaboration.addEvidence("不足：Q&A 环节被问'压力测试怎么做'，答不上来");

        // 打印统计
        System.out.println("=== 能力评估 ===");
        System.out.println("平均分: " + String.format("%.1f", assessment.getAverageScore()));
        System.out.println("等级: " + assessment.getLevel());
        System.out.println("提升: " + String.format("%.0f%%",
            (assessment.getAverageScore() - assessment.getInitialAverageScore())
            / assessment.getInitialAverageScore() * 100));

        // 导出报告
        assessment.exportToMarkdown("skill_radar.md");

        System.out.println("\n评估完成！");
    }
}
