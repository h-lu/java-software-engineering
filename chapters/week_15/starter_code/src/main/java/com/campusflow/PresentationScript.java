package com.campusflow;

import java.util.ArrayList;
import java.util.List;

/**
 * 演示脚本类 - 用于验证演示脚本的完整性和时间分配
 *
 * Week 15: 项目集市与展示准备
 * 本类用于验证演示脚本是否符合要求
 */
public class PresentationScript {

    private String title;
    private List<ScriptSection> sections;
    private int totalDurationMinutes;

    public PresentationScript(String title, int totalDurationMinutes) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("演示标题不能为空");
        }
        if (totalDurationMinutes < 8 || totalDurationMinutes > 10) {
            throw new IllegalArgumentException("演示时长必须在 8-10 分钟之间");
        }
        this.title = title;
        this.totalDurationMinutes = totalDurationMinutes;
        this.sections = new ArrayList<>();
    }

    /**
     * 添加脚本章节
     */
    public void addSection(String name, int durationMinutes, String description) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("章节名称不能为空");
        }
        if (durationMinutes <= 0) {
            throw new IllegalArgumentException("章节时长必须大于 0");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("章节描述不能为空");
        }
        sections.add(new ScriptSection(name, durationMinutes, description));
    }

    /**
     * 验证脚本时间分配是否合理
     * 开场: 1分钟, 主体: 7分钟, 结尾: 2分钟
     */
    public boolean validateTimeAllocation() {
        int total = sections.stream().mapToInt(s -> s.durationMinutes).sum();
        return total <= totalDurationMinutes;
    }

    /**
     * 获取各章节时长
     */
    public int getSectionCount() {
        return sections.size();
    }

    /**
     * 获取计划总时长
     * 如果没有添加章节，返回构造函数设置的总时长
     * 如果添加了章节，返回所有章节的时长总和
     */
    public int getTotalPlannedMinutes() {
        if (sections.isEmpty()) {
            return totalDurationMinutes;
        }
        return sections.stream().mapToInt(s -> s.durationMinutes).sum();
    }

    public String getTitle() {
        return title;
    }

    public List<ScriptSection> getSections() {
        return new ArrayList<>(sections);
    }

    /**
     * 脚本章节
     */
    public static class ScriptSection {
        private final String name;
        private final int durationMinutes;
        private final String description;

        public ScriptSection(String name, int durationMinutes, String description) {
            this.name = name;
            this.durationMinutes = durationMinutes;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public int getDurationMinutes() {
            return durationMinutes;
        }

        public String getDescription() {
            return description;
        }
    }
}
