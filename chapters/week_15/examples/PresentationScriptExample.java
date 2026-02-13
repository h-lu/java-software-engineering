/**
 * Week 15: 演示脚本准备示例
 *
 * 演示如何使用 PresentationScript 类来准备演示脚本
 */
public class PresentationScriptExample {

    public static void main(String[] args) {
        // 创建一个 10 分钟的演示脚本
        PresentationScript script = new PresentationScript("CampusFlow 演示", 10);

        // 添加章节
        script.addSection("开场", 1, "自我介绍和演示路线图");
        script.addSection("问题背景", 1, "为什么选择这个项目进行开发");
        script.addSection("技术亮点", 2, "技术选型决策和系统架构设计");
        script.addSection("功能演示", 2, "核心功能演示：创建、编辑、标记完成");
        script.addSection("工程实践", 1, "测试策略、文档维护和自动部署流程");
        script.addSection("踩坑与成长", 1, "Bug Bash 中发现的问题和 AI 协作经验");
        script.addSection("结尾", 2, "总结成果、展示源码和演示链接");

        // 验证时间分配
        System.out.println("=== 演示脚本 ===");
        System.out.println("标题: " + script.getTitle());
        System.out.println("总时长: " + script.getTotalPlannedMinutes() + " 分钟");
        System.out.println("章节数量: " + script.getSectionCount());

        System.out.println("\n=== 章节安排 ===");
        for (var section : script.getSections()) {
            System.out.printf("- %s (%d 分钟): %s%n",
                section.getName(),
                section.getDurationMinutes(),
                section.getDescription());
        }

        if (script.validateTimeAllocation()) {
            System.out.println("\n时间分配合理！");
        } else {
            System.out.println("\n警告：时间分配超出限制！");
        }
    }
}
