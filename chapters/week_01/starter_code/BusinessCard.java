package com.campusflow;

import java.util.Scanner;

/**
 * Week 01 作业 - 名片生成器
 * 
 * 任务要求：
 * 1. 读取用户输入：姓名、职业、邮箱、年龄
 * 2. 使用 ASCII 字符绘制边框，格式化输出名片
 * 3. 实现输入验证：
 *    - 邮箱必须包含 "@" 符号
 *    - 年龄必须在 1-120 之间
 * 
 * 示例输出：
 * ╔════════════════════════════════╗
 * ║         个人名片               ║
 * ╠════════════════════════════════╣
 * ║  姓名：张三                    ║
 * ║  职业：软件工程师              ║
 * ║  邮箱：zhangsan@example.com    ║
 * ║  年龄：22                      ║
 * ╚════════════════════════════════╝
 */
public class BusinessCard {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== 名片生成器 ===\n");
        
        // TODO: 1. 读取用户输入
        // 提示：使用 scanner.nextLine() 读取字符串
        // 提示：使用 Integer.parseInt() 将字符串转换为整数
        
        // TODO: 2. 实现输入验证
        // 提示：邮箱验证 - 使用 String.contains("@")
        // 提示：年龄验证 - 检查范围 1-120
        // 提示：如果输入无效，提示重新输入
        
        // TODO: 3. 打印格式化的名片
        // 提示：使用 "═".repeat(30) 生成边框
        // 提示：使用字符串拼接或 String.format() 格式化
        
        // TODO: 4. 关闭 Scanner
        
    }
    
    /**
     * 提示：可以在这里添加辅助方法，例如：
     * - 格式化输出一行
     * - 居中对齐文本
     * - 左对齐并填充空格
     */
}
