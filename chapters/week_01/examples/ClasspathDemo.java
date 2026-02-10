package com.campusflow;

import java.util.Scanner;

/**
 * 小北的错误示例：Classpath 问题演示
 * 
 * 这个类展示了初学者常遇到的包声明问题。
 * 
 * 错误场景：
 * 1. 文件路径：src/HelloWorldBroken.java（没有包目录）
 * 2. 但文件中有：package com.campusflow;
 * 3. 编译后运行：java HelloWorldBroken
 * 4. 报错：Could not find or load main class HelloWorldBroken
 * 
 * 正确做法：
 * 1. 文件路径：src/com/campusflow/HelloWorldCorrect.java
 * 2. 编译：javac com/campusflow/HelloWorldCorrect.java
 * 3. 运行：java com.campusflow.HelloWorldCorrect
 * 
 * 或者使用 Maven/IDE，它们会自动处理包结构。
 * 
 * @author CampusFlow Team
 */
public class ClasspathDemo {
    
    public static void main(String[] args) {
        System.out.println("如果你看到这个输出，说明类路径配置正确！");
        System.out.println("完整类名：com.campusflow.ClasspathDemo");
        System.out.println("\n常见错误：");
        System.out.println("1. 包声明与目录结构不匹配");
        System.out.println("2. 运行时使用了错误的类名");
        System.out.println("3. 类路径（classpath）设置错误");
    }
}
