import java.util.Scanner;

public class ProfileCard {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // 获取用户输入
        System.out.print("请输入姓名：");
        String name = scanner.nextLine();
        
        System.out.print("请输入年龄：");
        int age = scanner.nextInt();
        scanner.nextLine();  // 吃掉换行符
        
        System.out.print("请输入专业：");
        String major = scanner.nextLine();
        
        System.out.print("请输入邮箱：");
        String email = scanner.nextLine();
        
        // 输出个人信息卡片
        System.out.println("\n================================");
        System.out.println("         个人信息卡片");
        System.out.println("================================");
        System.out.println("姓名：" + name);
        System.out.println("年龄：" + age + " 岁");
        System.out.println("专业：" + major);
        System.out.println("邮箱：" + email);
        System.out.println("================================");
        
        scanner.close();
    }
}
