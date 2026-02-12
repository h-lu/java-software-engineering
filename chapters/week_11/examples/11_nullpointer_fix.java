/*
 * 示例：SpotBugs 发现的空指针问题与修复
 * 运行方式：阅读参考，理解问题类型和修复方法
 * 预期输出：理解 SpotBugs 如何报告和定位问题
 *
 * 本例演示：
 * 1. SpotBugs 发现的常见空指针问题
 * 2. 问题代码与修复代码对比
 * 3. 防御式编程最佳实践
 */
package examples;

/**
 * 空指针问题与修复示例。
 *
 * <p>SpotBugs 使用 "NP_NULL_ON_SOME_PATH" 模式检测潜在的空指针解引用。
 * 这些问题不会立即导致崩溃，但在特定输入下会抛出 NullPointerException。
 */
public class _11_nullpointer_fix {

    /**
     * 问题 1：未检查返回值是否为 null
     *
     * <p>SpotBugs 报告：
     * <pre>
     * NP_NULL_ON_SOME_PATH: Possible null pointer dereference
     * 在 taskRepository.findById() 返回 null 时，下一行调用方法会 NPE
     * </pre>
     */
    static class Bug1 {
        static class TaskRepository {
            Task findById(String id) {
                // 可能返回 null（任务不存在）
                return null;
            }
        }

        static class Task {
            void setTitle(String title) {}
        }

        // ❌ 问题代码
        static class BadController {
            private TaskRepository taskRepository = new TaskRepository();

            public void updateTask(String id, Task task) {
                Task existing = taskRepository.findById(id);
                // 如果 findById 返回 null，下一行会抛 NullPointerException
                existing.setTitle(task.getTitle());
            }
        }

        // ✅ 修复代码
        static class GoodController {
            private TaskRepository taskRepository = new TaskRepository();

            public void updateTask(String id, Task task) {
                Task existing = taskRepository.findById(id);
                if (existing == null) {
                    throw new NotFoundException("Task not found: " + id);
                }
                existing.setTitle(task.getTitle());
            }
        }
    }

    /**
     * 问题 2：未检查方法参数是否为 null
     *
     * <p>SpotBugs 报告：
     * <pre>
     * NP_PARAMETER_MUST_BE_NONNULL_BUT_MARKED_AS_NULLABLE
     * 参数可能为 null，但方法假设它不为 null
     * </pre>
     */
    static class Bug2 {
        // ❌ 问题代码
        static class BadService {
            public String normalizeName(String name) {
                // 如果 name 为 null，会 NPE
                return name.trim().toLowerCase();
            }
        }

        // ✅ 修复代码 1：防御式检查
        static class GoodService1 {
            public String normalizeName(String name) {
                if (name == null || name.isEmpty()) {
                    return "";
                }
                return name.trim().toLowerCase();
            }
        }

        // ✅ 修复代码 2：使用 Objects.requireNonNull
        static class GoodService2 {
            public String normalizeName(String name) {
                // 快速失败，明确要求参数不为 null
                return requireNonNull(name, "name must not be null")
                    .trim()
                    .toLowerCase();
            }

            private <T> T requireNonNull(T obj, String message) {
                if (obj == null) {
                    throw new NullPointerException(message);
                }
                return obj;
            }
        }
    }

    /**
     * 问题 3：链式调用中的空指针
     *
     * <p>SpotBugs 报告：
     * <pre>
     * NP_NULL_ON_SOME_PATH_EXCEPTION: Possible null pointer dereference in method
     * 链式调用中任何一环返回 null 都会导致 NPE
     * </pre>
     */
    static class Bug3 {
        static class User {
            Address getAddress() { return null; }
        }

        static class Address {
            String getCity() { return null; }
        }

        // ❌ 问题代码
        static class BadService {
            public String getUserCity(User user) {
                // user 可能为 null，getAddress() 可能为 null，getCity() 可能为 null
                return user.getAddress().getCity();
            }
        }

        // ✅ 修复代码 1：防御式检查
        static class GoodService1 {
            public String getUserCity(User user) {
                if (user == null || user.getAddress() == null) {
                    return "Unknown";
                }
                String city = user.getAddress().getCity();
                return city != null ? city : "Unknown";
            }
        }

        // ✅ 修复代码 2：使用 Optional（Java 8+）
        static class GoodService2 {
            public String getUserCity(User user) {
                return Optional.ofNullable(user)
                    .map(User::getAddress)
                    .map(Address::getCity)
                    .orElse("Unknown");
            }
        }
    }

    /**
     * 问题 4：集合操作中的空指针
     *
     * <p>SpotBugs 报告：
     * <pre>
     * NP_UNKNOWN_NULLNESS_WARNING: Unknown nullness
     * 集合可能为 null 或元素可能为 null
     * </pre>
     */
    static class Bug4 {
        // ❌ 问题代码
        static class BadService {
            public void printFirstItem(java.util.List<String> items) {
                // items 可能为 null
                System.out.println(items.get(0));
            }
        }

        // ✅ 修复代码
        static class GoodService {
            public void printFirstItem(java.util.List<String> items) {
                if (items == null || items.isEmpty()) {
                    System.out.println("No items");
                    return;
                }
                System.out.println(items.get(0));
            }
        }
    }

    /**
     * 问题 5：自动拆箱的空指针
     *
     * <p>SpotBugs 报告：
     * <pre>
     * NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE: Unboxing of null value
     * Integer 等包装类型为 null 时，自动拆箱会 NPE
     * </pre>
     */
    static class Bug5 {
        // ❌ 问题代码
        static class BadService {
            private java.util.Map<String, Integer> scores = new java.util.HashMap<>();

            public int getScore(String key) {
                // get() 可能返回 null，赋值给 int 时自动拆箱会 NPE
                return scores.get(key);
            }
        }

        // ✅ 修复代码 1：使用 getOrDefault
        static class GoodService1 {
            private java.util.Map<String, Integer> scores = new java.util.HashMap<>();

            public int getScore(String key) {
                return scores.getOrDefault(key, 0);
            }
        }

        // ✅ 修复代码 2：显式检查
        static class GoodService2 {
            private java.util.Map<String, Integer> scores = new java.util.HashMap<>();

            public int getScore(String key) {
                Integer value = scores.get(key);
                return value != null ? value : 0;
            }
        }
    }

    // ================== 辅助类 ==================

    static class NotFoundException extends RuntimeException {
        public NotFoundException(String message) {
            super(message);
        }
    }

    // Optional 导入（用于示例 3）
    import java.util.Optional;
}

/*
 * SpotBugs 问题类型速查：
 *
 * NP_NULL_ON_SOME_PATH
 * - 含义：在某些执行路径上可能解引用 null
 * - 修复：添加 null 检查
 *
 * NP_PARAMETER_MUST_BE_NONNULL_BUT_MARKED_AS_NULLABLE
 * - 含义：参数可能为 null，但代码假设它不为 null
 * - 修复：添加参数验证或 @Nullable 注解
 *
 * NP_NULL_ON_SOME_PATH_EXCEPTION
 * - 含义：异常处理路径中可能解引用 null
 * - 修复：在 catch 块中检查 null
 *
 * RCN_REDUNDANT_NULLCHECK_WOULD_HAVE_BEEN_A_NPE
 * - 含义：冗余的 null 检查，但检查之后还是会 NPE
 * - 修复：修复逻辑错误
 *
 * NP_UNKNOWN_NULLNESS_WARNING
 * - 含义：未知是否为 null（需要更仔细的分析）
 * - 修复：添加防御式检查或添加 @NonNull/@Nullable 注解
 *
 * 防御式编程原则：
 * 1. 永远假设外部输入可能为 null
 * 2. 使用 requireNonNull 快速失败（public API）
 * 3. 使用 Optional 处理可能为空的返回值
 * 4. 使用 getOrDefault 处理可能为空的集合元素
 * 5. 链式调用前检查每一环
 *
 * 选择策略：
 * - 内部工具方法：使用 requireNonNull 快速失败
 * - 公共 API：返回 null 或 Optional，在文档中说明
 * - 集合操作：使用 getOrDefault/isEmpty 检查
 * - 链式调用：使用 Optional 或分步检查
 */
