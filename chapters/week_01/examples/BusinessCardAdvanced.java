package com.campusflow;

import java.util.Scanner;

/**
 * 名片生成器 - 增强版本（带输入验证）
 * 
 * 演示：
 * - 使用循环实现反复输入
 * - 基本的输入验证（邮箱格式、年龄范围）
 * - 异常处理初步
 * 
 * @author CampusFlow Team
 * @version 2.0
 */
public class BusinessCardAdvanced {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== 名片生成器（增强版）===\n");
        
        // 读取并验证各项输入
        String name = readNonEmptyString(scanner, "请输入姓名：");
        String job = readNonEmptyString(scanner, "请输入职业：");
        String email = readValidEmail(scanner);
        int age = readValidAge(scanner);
        
        // 打印名片
        printCard(name, job, email, age);
        
        scanner.close();
    }
    
    /**
     * 读取非空字符串
     */
    public static String readNonEmptyString(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("输入不能为空，请重新输入。");
        }
    }
    
    /**
     * 读取并验证邮箱格式
     */
    public static String readValidEmail(Scanner scanner) {
        while (true) {
            System.out.print("请输入邮箱：");
            String email = scanner.nextLine().trim();
            
            // 简单验证：必须包含 @ 符号
            if (email.contains("@") && email.indexOf("@") > 0 && email.indexOf("@") < email.length() - 1) {
                return email;
            }
            System.out.println("邮箱格式不正确，请重新输入（需要包含 @ 符号）。");
        }
    }
    
    /**
     * 读取并验证年龄
     */
    public static int readValidAge(Scanner scanner) {
        while (true) {
            System.out.print("请输入年龄（1-120）：");
            String input = scanner.nextLine().trim();
            
            try {
                int age = Integer.parseInt(input);
                if (age >= 1 && age <= 120) {
                    return age;
                }
                System.out.println("年龄必须在 1-120 之间。");
            } catch (NumberFormatException e) {
                System.out.println("请输入有效的数字。");
            }
        }
    }
    
    /**
     * 打印格式化的名片
     */
    public static void printCard(String name, String job, String email, int age) {
        String border = "═".repeat(32);
        
        System.out.println("\n╔" + border + "╗");
        System.out.println("║" + centerText("★ 个人名片 ★", 32) + "║");
        System.out.println("╠" + border + "╣");
        System.out.println(formatLine("姓名", name));
        System.out.println(formatLine("职业", job));
        System.out.println(formatLine("邮箱", email));
        System.out.println(formatLine("年龄", String.valueOf(age)));
        System.out.println("╚" + border + "╝");
    }
    
    /**
     * 格式化一行信息
     */
    public static String formatLine(String label, String value) {
        String content = "  " + label + "：" + value;
        return "║" + padRight(content, 32) + "║";
    }
    
    /**
     * 将文本居中
     */
    public static String centerText(String text, int width) {
        int padding = (width - text.length()) / 2;
        return " ".repeat(padding) + text + " ".repeat(width - text.length() - padding);
    }
    
    /**
     * 左对齐，右侧填充
     */
    public static String padRight(String text, int width) {
        if (text.length() >= width) {
            return text.substring(0, width);
        }
        return text + " ".repeat(width - text.length());
    }
}
