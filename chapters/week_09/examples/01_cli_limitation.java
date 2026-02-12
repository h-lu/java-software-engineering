/*
 * 示例：展示 CLI（命令行界面）应用的局限性。
 * 本例演示：为什么需要将 CLI 应用改造为 Web 服务。
 * 运行方式：javac 01_cli_limitation.java && java CliLimitationDemo
 * 预期输出：展示 CLI 的局限性和 Web 服务的优势对比
 */

import java.util.Scanner;

// 文件：CliTaskManager.java（CLI 版任务管理器 - 反例展示）
// 问题：只能在本地运行，无法远程访问，无法支持多用户
class CliTaskManager {
    private final java.util.Map<String, String> tasks = new java.util.HashMap<>();
    private int nextId = 1;

    public void run() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== CampusFlow CLI 版 ===");
        System.out.println("局限性演示：");
        System.out.println("1. 只能在本地运行");
        System.out.println("2. 用户必须安装 Java");
        System.out.println("3. 无法在手机/平板上使用");
        System.out.println("4. 无法支持多用户同时访问");
        System.out.println();

        while (true) {
            System.out.println("\n命令: [1]添加任务 [2]查看任务 [3]退出");
            System.out.print("> ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> addTask(scanner);
                case "2" -> listTasks();
                case "3" -> {
                    System.out.println("再见！");
                    return;
                }
                default -> System.out.println("无效命令");
            }
        }
    }

    private void addTask(Scanner scanner) {
        System.out.print("任务标题: ");
        String title = scanner.nextLine();
        String id = String.valueOf(nextId++);
        tasks.put(id, title);
        System.out.println("任务已添加，ID: " + id);
    }

    private void listTasks() {
        if (tasks.isEmpty()) {
            System.out.println("暂无任务");
            return;
        }
        System.out.println("\n任务列表:");
        tasks.forEach((id, title) -> System.out.println("  [" + id + "] " + title));
    }
}

// 文件：CliLimitationDemo.java（演示入口）
class CliLimitationDemo {
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║         CLI 应用的局限性 vs Web 服务的优势               ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println();

        // CLI 局限性分析
        System.out.println("【CLI 版 CampusFlow 的局限】");
        System.out.println();
        System.out.println("场景 1：小北想在手机上查看任务");
        System.out.println("  CLI: ❌ 无法使用，手机没有 Java 环境");
        System.out.println("  Web: ✅ 浏览器访问 http://server/tasks 即可");
        System.out.println();

        System.out.println("场景 2：室友想借用 CampusFlow 管理任务");
        System.out.println("  CLI: ❌ 需要安装 JDK、下载 jar、配置数据库");
        System.out.println("  Web: ✅ 直接访问网址，零安装");
        System.out.println();

        System.out.println("场景 3：团队协作，多人同时管理任务");
        System.out.println("  CLI: ❌ 单机运行，数据无法共享");
        System.out.println("  Web: ✅ 服务端集中存储，多人实时协作");
        System.out.println();

        System.out.println("场景 4：从外部系统导入任务");
        System.out.println("  CLI: ❌ 需要手动导出/导入文件");
        System.out.println("  Web: ✅ 提供 API，其他系统可直接调用");
        System.out.println();

        // 技术对比
        System.out.println("【技术架构对比】");
        System.out.println();
        System.out.println("特性              CLI 应用           Web 服务");
        System.out.println("────────────────  ────────────────   ────────────────");
        System.out.println("运行位置          本地计算机         服务器（24小时运行）");
        System.out.println("访问方式          终端/命令行        HTTP/浏览器/API");
        System.out.println("用户范围          单机用户           任何联网设备");
        System.out.println("并发支持          ❌ 不支持          ✅ 支持多用户");
        System.out.println("数据共享          ❌ 本地文件        ✅ 集中存储");
        System.out.println("集成能力          ❌ 有限            ✅ REST API");
        System.out.println();

        // 为什么需要 Web 服务
        System.out.println("【为什么需要 Web 服务？】");
        System.out.println();
        System.out.println("1. 可访问性");
        System.out.println("   - 任何有浏览器的设备都能使用");
        System.out.println("   - 不需要安装任何软件");
        System.out.println();

        System.out.println("2. 协作能力");
        System.out.println("   - 多人同时操作，数据实时同步");
        System.out.println("   - 权限管理，不同用户看到不同数据");
        System.out.println();

        System.out.println("3. 集成能力");
        System.out.println("   - 提供 REST API，其他系统可以调用");
        System.out.println("   - 可以与第三方服务集成（日历、邮件等）");
        System.out.println();

        System.out.println("4. 可维护性");
        System.out.println("   - 服务端统一更新，用户无感知");
        System.out.println("   - 数据集中备份，更安全");
        System.out.println();

        // 交互模式对比
        System.out.println("【交互模式对比】");
        System.out.println();
        System.out.println("CLI 应用：");
        System.out.println("  用户 ──启动──> 程序 ──交互──> 退出");
        System.out.println("   ↑___________________________↓");
        System.out.println("   （程序生命周期与用户会话绑定）");
        System.out.println();

        System.out.println("Web 服务：");
        System.out.println("  服务 ──启动────────────────────> 持续运行");
        System.out.println("   ↑      ↓      ↑      ↓");
        System.out.println("  用户A  响应A  用户B  响应B");
        System.out.println("   （服务长期运行，按需响应请求）");
        System.out.println();

        System.out.println("【本周目标】");
        System.out.println("将 CampusFlow 从 CLI 版改造为 Web 服务：");
        System.out.println("  1. 使用 Javalin 框架搭建 HTTP 服务");
        System.out.println("  2. 设计 RESTful API 端点");
        System.out.println("  3. 处理 JSON 数据交换");
        System.out.println("  4. 实现统一异常处理");
        System.out.println();

        // 可选：实际运行 CLI 演示
        System.out.print("是否运行 CLI 演示？(y/n): ");
        Scanner scanner = new Scanner(System.in);
        if (scanner.nextLine().trim().toLowerCase().startsWith("y")) {
            System.out.println();
            new CliTaskManager().run();
        } else {
            System.out.println("\n跳过 CLI 演示，直接学习 Web 服务开发！");
        }
    }
}
