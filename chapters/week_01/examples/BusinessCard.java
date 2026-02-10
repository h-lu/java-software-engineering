package com.campusflow;

import java.time.LocalDate;
import java.util.Scanner;

/**
 * 名片生成器 - 基础版本
 * 
 * 演示：
 * - 使用 Scanner 读取用户输入
 * - 使用 LocalDate 获取日期
 * - 基本的字符串拼接和格式化输出
 * 
 * @author CampusFlow Team
 * @version 1.0
 */
public class BusinessCard {
    
    public static void main(String[] args) {
        // 创建 Scanner 对象用于读取输入
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== 名片生成器 ===\n");
        
        // 读取用户信息
        System.out.print("请输入姓名：");
        String name = scanner.nextLine();
        
        System.out.print("请输入职业：");
        String job = scanner.nextLine();
        
        System.out.print("请输入邮箱：");
        String email = scanner.nextLine();
        
        System.out.print("请输入年龄：");
        int age = Integer.parseInt(scanner.nextLine());
        
        // 打印名片
        printCard(name, job, email, age);
        
        // 关闭 Scanner
        scanner.close();
    }
    
    /**
     * 打印格式化的名片
     */
    public static void printCard(String name, String job, String email, int age) {
        // 使用 String 的 repeat 方法生成边框
        String border = "═".repeat(30);
        
        System.out.println("\n╔" + border + "╗");
        System.out.println("║" + centerText("个人名片", 30) + "║");
        System.out.println("╠" + border + "╣");
        System.out.println("║  姓名：" + padRight(name, 20) + "║");
        System.out.println("║  职业：" + padRight(job, 20) + "║");
        System.out.println("║  邮箱：" + padRight(email, 20) + "║");
        System.out.println("║  年龄：" + padRight(String.valueOf(age), 20) + "║");
        System.out.println("╚" + border + "╝");
    }
    
    /**
     * 将文本居中，填充至指定宽度
     */
    public static String centerText(String text, int width) {
        int padding = (width - text.length()) / 2;
        return " ".repeat(padding) + text + " ".repeat(width - text.length() - padding);
    }
    
    /**
     * 将文本左对齐，右侧填充空格至指定宽度
     */
    public static String padRight(String text, int width) {
        if (text.length() >= width) {
            return text.substring(0, width);
        }
        return text + " ".repeat(width - text.length());
    }
}
