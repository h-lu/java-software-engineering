package com.campusflow;

/**
 * 静态类型 vs 动态类型对比示例
 * 
 * 这个类展示了 Java 静态类型系统的特点。
 * 对比 Python 代码：
 * 
 * Python:
 *   name = "Alice"      # str
 *   age = 25            # int
 *   age = "25"          # 可以，现在变成 str
 * 
 * Java:
 *   String name = "Alice";   // 只能是 String
 *   int age = 25;             // 只能是 int
 *   // age = "25";            // 编译错误！
 * 
 * @author CampusFlow Team
 */
public class TypeComparison {
    
    public static void main(String[] args) {
        // 1. 变量声明时必须指定类型
        String name = "Alice";
        int age = 25;
        double salary = 50000.50;
        boolean isStudent = true;
        
        System.out.println("姓名：" + name);
        System.out.println("年龄：" + age);
        System.out.println("薪资：" + salary);
        System.out.println("是否学生：" + isStudent);
        
        // 2. 类型不能随意改变（取消注释会导致编译错误）
        // age = "25";  // Error: incompatible types
        
        // 3. 需要显式类型转换
        int intValue = 10;
        double doubleValue = intValue;  // 自动转换（ widening ）
        System.out.println("自动转换 int -> double: " + doubleValue);
        
        double pi = 3.14159;
        int approximatePi = (int) pi;   // 强制转换（ narrowing ），会丢失精度
        System.out.println("强制转换 double -> int: " + approximatePi);
        
        // 4. 方法参数类型检查
        greet("Bob");  // 正常
        // greet(123);   // 编译错误！
        
        // 5. 编译期错误检测
        // System.out.println(undefinedVariable);  // 编译错误，不是运行时错误
    }
    
    /**
     * 方法参数类型是确定的
     */
    public static void greet(String name) {
        System.out.println("Hello, " + name + "!");
    }
    
    /**
     * 返回值类型是确定的
     */
    public static int add(int a, int b) {
        return a + b;
    }
    
    /**
     * 重载：同名方法，不同参数类型
     * Java 根据参数类型选择调用哪个方法
     */
    public static double add(double a, double b) {
        return a + b;
    }
}
