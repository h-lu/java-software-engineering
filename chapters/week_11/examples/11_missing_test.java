/*
 * 示例：JaCoCo 覆盖率盲区示例
 * 运行方式：mvn -f chapters/week_11/starter_code/pom.xml test jacoco:report
 * 预期输出：生成覆盖率报告，显示哪些代码未测试
 *
 * 本例演示：
 * 1. 未被测试覆盖的代码分支
 * 2. 如何补充测试用例
 * 3. 修复前后的覆盖率对比
 */
package examples;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 代码覆盖率盲区示例。
 *
 * <p>这个类包含多个方法，有些被测试覆盖，有些没有。
 * JaCoCo 会用红色高亮显示未覆盖的代码行。
 */
class _11_missing_test {

    /**
     * 示例 1：if-else 分支未完全覆盖
     *
     * <p>常见问题：只测试了 "happy path"，没有测试异常情况。
     */
    static class Example1 {
        private final Map<String, Integer> scores = new HashMap<>();

        /**
         * 获取分数，如果不存在则返回默认值。
         *
         * <p>🔴 覆盖率盲区：
         * <ul>
         *   <li>只测试了 key 存在的情况</li>
         *   <li>没有测试 key 不存在的情况（else 分支）</li>
         * </ul>
         */
        public int getScore(String key) {
            if (scores.containsKey(key)) {
                return scores.get(key);  // ✅ 被测试覆盖
            } else {
                return 0;  // 🔴 未测试：红色（else 分支没执行）
            }
        }

        public void setScore(String key, int score) {
            scores.put(key, score);
        }
    }

    /**
     * 示例 2：异常处理未覆盖
     *
     * <p>常见问题：没有测试异常抛出的情况。
     */
    static class Example2 {
        /**
         * 根据年龄判断是否可以投票。
         *
         * <p>🔴 覆盖率盲区：
         * <ul>
         *   <li>只测试了 age >= 18 的情况</li>
         *   <li>没有测试 age < 18 抛出异常的情况</li>
         * </ul>
         */
        public boolean canVote(int age) {
            if (age < 0) {
                throw new IllegalArgumentException("Age cannot be negative");  // 🔴 未测试
            }
            if (age < 18) {
                throw new IllegalArgumentException("You must be 18 or older to vote");  // 🔴 未测试
            }
            return true;  // ✅ 被测试覆盖
        }
    }

    /**
     * 示例 3：Optional 链式调用未覆盖
     *
     * <p>常见问题：只测试了有值的情况，没测试空值情况。
     */
    static class Example3 {
        private final Map<String, User> users = new HashMap<>();

        /**
         * 获取用户所在的城市。
         *
         * <p>🔴 覆盖率盲区：
         * <ul>
         *   <li>只测试了用户存在且有地址的情况</li>
         *   <li>没有测试用户不存在的情况（orElse 路径）</li>
         *   <li>没有测试地址为 null 的情况</li>
         * </ul>
         */
        public String getUserCity(String userId) {
            return Optional.ofNullable(users.get(userId))  // 🔴 如果 users.get 返回 null
                .map(user -> user.getAddress())  // 🔴 如果 getAddress 返回 null
                .map(Address::getCity)  // 🔴 如果 getCity 返回 null
                .orElse("Unknown");  // 🔴 orElse 路径未测试
        }

        public void addUser(User user) {
            users.put(user.getId(), user);
        }
    }

    /**
     * 示例 4：switch 语句未完全覆盖
     *
     * <p>常见问题：只测试了部分 case 分支。
     */
    static class Example4 {
        /**
         * 获取季节名称。
         *
         * <p>🔴 覆盖率盲区：
         * <ul>
         *   <li>只测试了 SPRING 和 SUMMER</li>
         *   <li>AUTUMN 和 WINTER 的 case 未测试</li>
         * </ul>
         */
        public String getSeasonName(int month) {
            switch (month) {
                case 12:
                case 1:
                case 2:
                    return "Winter";  // 🔴 未测试
                case 3:
                case 4:
                case 5:
                    return "Spring";  // ✅ 被测试覆盖
                case 6:
                case 7:
                case 8:
                    return "Summer";  // ✅ 被测试覆盖
                case 9:
                case 10:
                case 11:
                    return "Autumn";  // 🔴 未测试
                default:
                    throw new IllegalArgumentException("Invalid month");  // 🔴 未测试
            }
        }
    }

    /**
     * 示例 5：try-catch 块未覆盖
     *
     * <p>常见问题：没有触发异常，catch 块未测试。
     */
    static class Example5 {
        /**
         * 安全地解析数字。
         *
         * <p>🔴 覆盖率盲区：
         * <ul>
         *   <li>只测试了正常数字输入</li>
         *   <li>没有测试非法输入（catch 块）</li>
         * </ul>
         */
        public int safeParseInt(String value, int defaultValue) {
            try {
                return Integer.parseInt(value);  // ✅ 被测试覆盖
            } catch (NumberFormatException e) {
                return defaultValue;  // 🔴 未测试：catch 块
            }
        }
    }

    // ================== 测试用例（JUnit 5）==================

    /**
     * 补充测试用例，提高覆盖率。
     *
     * <p>注意：这些测试用例不是写在示例类中，而是单独的测试类。
     * 这里为了演示方便，把它们放在一起。
     */
    static class ExampleTests {

        // ========== 示例 1：补充 else 分支测试 ==========

        /**
         * ✅ 补充测试：key 不存在的情况
         *
         * <p>补充前：覆盖率 50%
         * <p>补充后：覆盖率 100%
         */
        void testGetScore_KeyNotExists() {
            Example1 example = new Example1();

            // 测试 else 分支
            int score = example.getScore("nonexistent");
            assert score == 0 : "Should return 0 for nonexistent key";
        }

        // ========== 示例 2：补充异常测试 ==========

        /**
         * ✅ 补充测试：年龄小于 0
         *
         * <p>补充前：覆盖率 20%
         * <p>补充后：覆盖率 100%
         */
        void testCanVote_NegativeAge() {
            Example2 example = new Example2();

            try {
                example.canVote(-1);
                assert false : "Should throw IllegalArgumentException";
            } catch (IllegalArgumentException e) {
                assert e.getMessage().contains("negative");
            }
        }

        /**
         * ✅ 补充测试：年龄小于 18
         */
        void testCanVote_Under18() {
            Example2 example = new Example2();

            try {
                example.canVote(16);
                assert false : "Should throw IllegalArgumentException";
            } catch (IllegalArgumentException e) {
                assert e.getMessage().contains("18");
            }
        }

        // ========== 示例 3：补充 Optional 链测试 ==========

        /**
         * ✅ 补充测试：用户不存在
         *
         * <p>补充前：覆盖率 0%（因为 orElse 路径从未执行）
         * <p>补充后：覆盖率 100%
         */
        void testGetUserCity_UserNotExists() {
            Example3 example = new Example3();

            String city = example.getUserCity("nonexistent");
            assert "Unknown".equals(city) : "Should return Unknown for nonexistent user";
        }

        /**
         * ✅ 补充测试：用户存在但地址为 null
         */
        void testGetUserCity_AddressNull() {
            Example3 example = new Example3();
            User user = new User("user1", null);
            example.addUser(user);

            String city = example.getUserCity("user1");
            assert "Unknown".equals(city) : "Should return Unknown when address is null";
        }

        // ========== 示例 4：补充 switch 分支测试 ==========

        /**
         * ✅ 补充测试：冬季月份
         *
         * <p>补充前：覆盖率 40%
         * <p>补充后：覆盖率 100%
         */
        void testGetSeasonName_Winter() {
            Example4 example = new Example4();

            assert "Winter".equals(example.getSeasonName(12));
            assert "Winter".equals(example.getSeasonName(1));
            assert "Winter".equals(example.getSeasonName(2));
        }

        /**
         * ✅ 补充测试：秋季月份
         */
        void testGetSeasonName_Autumn() {
            Example4 example = new Example4();

            assert "Autumn".equals(example.getSeasonName(9));
            assert "Autumn".equals(example.getSeasonName(10));
            assert "Autumn".equals(example.getSeasonName(11));
        }

        /**
         * ✅ 补充测试：非法月份
         */
        void testGetSeasonName_Invalid() {
            Example4 example = new Example4();

            try {
                example.getSeasonName(13);
                assert false : "Should throw IllegalArgumentException";
            } catch (IllegalArgumentException e) {
                // Expected
            }
        }

        // ========== 示例 5：补充异常处理测试 ==========

        /**
         * ✅ 补充测试：非法数字输入
         *
         * <p>补充前：覆盖率 50%
         * <p>补充后：覆盖率 100%
         */
        void testSafeParseInt_InvalidInput() {
            Example5 example = new Example5();

            int result = example.safeParseInt("abc", 42);
            assert result == 42 : "Should return default value for invalid input";
        }
    }

    // ================== 辅助类 ==================

    static class User {
        private final String id;
        private final Address address;

        User(String id, Address address) {
            this.id = id;
            this.address = address;
        }

        String getId() { return id; }
        Address getAddress() { return address; }
    }

    static class Address {
        private final String city;

        Address(String city) {
            this.city = city;
        }

        String getCity() { return city; }
    }
}

/*
 * JaCoCo 覆盖率报告阅读指南：
 *
 * 1. 打开报告：open target/site/jacoco/index.html
 *
 * 2. 颜色含义：
 *    - 🟢 绿色：已覆盖（Covered）
 *    - 🔴 红色：未覆盖（Not Covered）
 *    - 🟡 黄色：部分覆盖（如 if 只覆盖了 true 分支）
 *
 * 3. 覆盖率指标：
 *    - Line Coverage：代码行执行比例
 *    - Branch Coverage：if/switch 分支执行比例（更重要！）
 *    - Method Coverage：方法调用比例
 *    - Class Coverage：类加载比例
 *
 * 4. 常见盲区类型：
 *    - 异常路径（throw new ...）
 *    - else 分支
 *    - null 检查分支
 *    - try-catch 块
 *    - switch case 分支
 *    - Optional.orElse/orElseGet
 *    - 短路运算符（&&, ||）
 *
 * 5. 补充测试优先级：
 *    P0: 核心业务逻辑的异常路径
 *    P1: 安全相关的检查（权限、参数验证）
 *    P2: 边界情况（空值、极值）
 *    P3: 工具类的所有分支
 *
 * 6. 不要追求 100% 覆盖率的原因：
 *    - getter/setter 不必全部测试
 *    - 某些异常确实是"不可能发生"的
 *    - 时间/环境相关的代码（如 System.currentTimeMillis()）
 *    - 遗留代码的重构成本
 *
 * 7. 工程实用主义：
 *    - 核心 API: 80%+ 覆盖率
 *    - 业务逻辑: 75%+ 覆盖率
 *    - 工具类: 85%+ 覆盖率
 *    - DTO/实体: 50%+ 覆盖率
 */
