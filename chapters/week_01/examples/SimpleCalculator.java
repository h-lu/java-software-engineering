import java.util.Scanner;

public class SimpleCalculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("请输入第一个数字：");
        double num1 = scanner.nextDouble();
        
        System.out.print("请输入运算符 (+, -, *, /)：");
        char operator = scanner.next().charAt(0);
        
        System.out.print("请输入第二个数字：");
        double num2 = scanner.nextDouble();
        
        double result = 0;
        boolean isValid = true;
        
        switch (operator) {
            case '+':
                result = num1 + num2;
                break;
            case '-':
                result = num1 - num2;
                break;
            case '*':
                result = num1 * num2;
                break;
            case '/':
                if (num2 != 0) {
                    result = num1 / num2;
                } else {
                    System.out.println("错误：不能除以零！");
                    isValid = false;
                }
                break;
            default:
                System.out.println("错误：无效的运算符！");
                isValid = false;
        }
        
        if (isValid) {
            System.out.println("结果：" + num1 + " " + operator + " " + num2 + " = " + result);
        }
        
        scanner.close();
    }
}
