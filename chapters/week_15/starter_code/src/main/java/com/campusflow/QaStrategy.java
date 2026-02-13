package com.campusflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 问答策略类 - 用于验证问答准备的完整性
 *
 * Week 15: 项目集市与展示准备
 * 本类用于验证是否准备了常见问题的回答
 */
public class QaStrategy {

    private Map<String, String> qaPairs;

    public QaStrategy() {
        this.qaPairs = new HashMap<>();
    }

    /**
     * 添加问答对
     */
    public void addQaPair(String question, String answer) {
        if (question == null || question.trim().isEmpty()) {
            throw new IllegalArgumentException("问题不能为空");
        }
        if (answer == null || answer.trim().isEmpty()) {
            throw new IllegalArgumentException("回答不能为空");
        }
        // 检查回答是否过短（少于 20 字符）
        if (answer.length() < 20) {
            throw new IllegalArgumentException("回答过于简短，请提供详细解释（至少 20 字符）");
        }
        qaPairs.put(question, answer);
    }

    /**
     * 获取回答
     */
    public String getAnswer(String question) {
        return qaPairs.get(question);
    }

    /**
     * 验证问答数量是否足够（至少 10 个）
     */
    public boolean hasEnoughQaPairs() {
        return qaPairs.size() >= 10;
    }

    /**
     * 检查是否包含关键问题类型
     */
    public boolean hasTechnicalQuestions() {
        return qaPairs.keySet().stream()
                .anyMatch(q -> q.contains("技术") || q.contains("为什么") || q.contains("选型"));
    }

    public boolean hasDesignQuestions() {
        return qaPairs.keySet().stream()
                .anyMatch(q -> q.contains("设计") || q.contains("架构") || q.contains("模型"));
    }

    public boolean hasPracticeQuestions() {
        return qaPairs.keySet().stream()
                .anyMatch(q -> q.contains("工程") || q.contains("测试") || q.contains("部署"));
    }

    public boolean hasFutureQuestions() {
        return qaPairs.keySet().stream()
                .anyMatch(q -> q.contains("未来") || q.contains("计划") || q.contains("下一步"));
    }

    /**
     * 验证问答准备是否完整（包含所有类型的问题）
     */
    public boolean isComplete() {
        return hasEnoughQaPairs()
                && hasTechnicalQuestions()
                && hasDesignQuestions()
                && hasPracticeQuestions()
                && hasFutureQuestions();
    }

    /**
     * 获取所有问题
     */
    public List<String> getAllQuestions() {
        return new ArrayList<>(qaPairs.keySet());
    }

    public int getQaCount() {
        return qaPairs.size();
    }
}
