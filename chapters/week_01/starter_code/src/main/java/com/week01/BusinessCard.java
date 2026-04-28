package com.week01;

import java.util.Scanner;

/**
 * 练习 5：完整名片生成器骨架。
 */
public class BusinessCard {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== 名片生成器 ===");
        System.out.print("请输入姓名：");
        String name = scanner.nextLine();

        // TODO: 继续读取职位、邮箱、年龄、工作年限。
        // TODO: 调用 formatCard(...) 生成最终名片并输出。
        System.out.println("TODO: 你好，" + name + "。请补全 BusinessCard.java。");

        scanner.close();
    }

    public static String formatCard(String name, String jobTitle, String email, int age, double yearsOfExperience) {
        // TODO: 返回 ASSIGNMENT.md 中要求的多行名片文本。
        return "TODO: format business card for " + name;
    }
}
