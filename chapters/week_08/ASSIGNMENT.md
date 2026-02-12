# Week 08 作业：代码重构与设计模式实践

## 作业概述

本周你将学习如何让代码"活得更久"——通过识别代码坏味道、应用重构手法、引入设计模式来提升代码质量。作业围绕一个"有味道"的代码库展开，你将亲手体验从混乱到清晰的改造过程。

**预计耗时**：5-7 小时
**截止日期**：2026-02-22（周日）23:59

---

## 学习目标关联

完成本作业后，你将能够：
- 识别常见的代码坏味道（上帝类、重复代码、长方法等）
- 应用提取方法、移动方法等重构手法改进代码结构
- 使用策略模式消除复杂的条件判断
- 使用 SpotBugs/PMD 进行静态代码分析
- 理解技术债的概念，编写架构决策记录（ADR）

---

## 基础作业（必做，60 分）

### 任务 1：代码坏味道识别

下面是一段有严重质量问题的 `OrderProcessor` 类代码。请仔细阅读，识别其中存在的代码坏味道。

```java
public class OrderProcessor {
    private final String dbUrl = "jdbc:sqlite:orders.db";
    private List<Order> cache = new ArrayList<>();

    public void processOrder(String customerId, List<String> items, String paymentType,
                            String shippingMethod, boolean isVip, String couponCode) {
        // 验证输入
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException("客户ID不能为空");
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("订单不能为空");
        }

        // 计算总价
        double total = 0.0;
        for (String item : items) {
            double price = 0.0;
            if (item.equals("book")) {
                price = 50.0;
            } else if (item.equals("pen")) {
                price = 10.0;
            } else if (item.equals("notebook")) {
                price = 20.0;
            } else {
                price = 30.0;
            }
            total += price;
        }

        // VIP折扣
        if (isVip) {
            total = total * 0.9;
        }

        // 优惠券折扣
        if (couponCode != null && !couponCode.isEmpty()) {
            if (couponCode.equals("SAVE10")) {
                total = total * 0.9;
            } else if (couponCode.equals("SAVE20")) {
                total = total * 0.8;
            } else if (couponCode.equals("HALF")) {
                total = total * 0.5;
            }
        }

        // 运费计算
        double shippingCost = 0.0;
        if (shippingMethod.equals("express")) {
            shippingCost = 20.0;
            if (total > 200) {
                shippingCost = 0;
            }
        } else if (shippingMethod.equals("standard")) {
            shippingCost = 10.0;
            if (total > 100) {
                shippingCost = 0;
            }
        } else if (shippingMethod.equals("pickup")) {
            shippingCost = 0;
        }
        total += shippingCost;

        // 支付方式处理
        String paymentResult;
        if (paymentType.equals("credit_card")) {
            paymentResult = "信用卡支付: " + total;
        } else if (paymentType.equals("alipay")) {
            paymentResult = "支付宝支付: " + total;
        } else if (paymentType.equals("wechat")) {
            paymentResult = "微信支付: " + total;
        } else {
            paymentResult = "未知支付方式";
        }

        // 保存到数据库（重复代码！）
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(
                 "INSERT INTO orders (customer_id, total, payment_type, status) VALUES (?, ?, ?, ?)")) {
            pstmt.setString(1, customerId);
            pstmt.setDouble(2, total);
            pstmt.setString(3, paymentType);
            pstmt.setString(4, "completed");
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("保存订单失败", e);
        }

        // 更新缓存
        Order order = new Order(customerId, items, total, paymentType);
        cache.add(order);

        // 发送通知
        System.out.println("订单处理完成: " + paymentResult);
    }

    public List<Order> getCustomerOrders(String customerId) {
        // 又是重复的数据库代码！
        List<Order> result = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(
                 "SELECT * FROM orders WHERE customer_id = ?")) {
            pstmt.setString(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Order order = new Order(
                    rs.getString("customer_id"),
                    new ArrayList<>(),
                    rs.getDouble("total"),
                    rs.getString("payment_type")
                );
                result.add(order);
            }
        } catch (SQLException e) {
            throw new RuntimeException("查询订单失败", e);
        }
        return result;
    }
}
```

**要求**：

1. **识别至少 5 种代码坏味道**
   - 上帝类（God Class）
   - 重复代码（Duplicated Code）
   - 长方法（Long Method）
   - 其他你能识别的坏味道

2. **说明每种坏味道的危害**
   - 为什么这种写法有问题？
   - 会给维护带来什么困难？

3. **给出改进建议**
   - 针对每种坏味道，建议用什么重构手法改进？

**提交格式**：

```markdown
## 代码坏味道分析报告

### 1. 上帝类
- **位置**：OrderProcessor 类
- **问题描述**：...
- **危害**：...
- **改进建议**：...

### 2. 重复代码
...
```

---

### 任务 2：应用重构手法改进代码

基于任务 1 的分析，对 `OrderProcessor` 进行重构。

**要求**：

1. **提取方法（Extract Method）**
   - 将 `processOrder` 拆分为多个小方法
   - 每个方法只做一件事
   - 方法命名要清晰表达意图

2. **移动方法（Move Method）**
   - 将数据库操作移到 `OrderRepository` 类
   - 将价格计算逻辑移到合适的位置

3. **消除重复代码**
   - 消除数据库连接代码的重复
   - 消除其他重复逻辑

**重构后的代码结构示例**：

```java
// OrderRepository.java - 负责数据访问
public class OrderRepository {
    private final String dbUrl;

    public OrderRepository(String dbUrl) { ... }
    public void save(Order order) { ... }
    public List<Order> findByCustomerId(String customerId) { ... }
}

// PriceCalculator.java - 负责价格计算
public class PriceCalculator {
    public double calculateItemPrice(String item) { ... }
    public double applyDiscount(double total, boolean isVip, String couponCode) { ... }
}

// ShippingCalculator.java - 负责运费计算
public class ShippingCalculator {
    public double calculateShipping(String method, double orderTotal) { ... }
}

// OrderProcessor.java - 只负责协调
public class OrderProcessor {
    private final OrderRepository repository;
    private final PriceCalculator priceCalculator;
    private final ShippingCalculator shippingCalculator;

    public OrderProcessor(OrderRepository repository,
                         PriceCalculator priceCalculator,
                         ShippingCalculator shippingCalculator) {
        this.repository = repository;
        this.priceCalculator = priceCalculator;
        this.shippingCalculator = shippingCalculator;
    }

    public void processOrder(String customerId, List<String> items,
                            String paymentType, String shippingMethod,
                            boolean isVip, String couponCode) {
        // 现在这个方法应该很简洁，只负责协调各个组件
    }
}
```

**输入/输出示例**：

```
输入：
- customerId: "C001"
- items: ["book", "pen", "notebook"]
- paymentType: "alipay"
- shippingMethod: "standard"
- isVip: true
- couponCode: "SAVE10"

计算过程：
1. 商品价格：50 + 10 + 20 = 80
2. VIP折扣：80 * 0.9 = 72
3. 优惠券：72 * 0.9 = 64.8
4. 运费：standard方式，订单<100，运费10
5. 总计：64.8 + 10 = 74.8

输出：订单保存成功，返回订单对象（包含总价74.8）
```

**提示**：
- 重构前确保有测试保护（可以先写测试）
- 小步前进，每次重构后运行测试
- 使用 IDE 的重构快捷键（Ctrl+Alt+M 提取方法）

---

### 任务 3：使用策略模式重构折扣计算

当前 `OrderProcessor` 中的折扣计算使用了一堆 if-else：

```java
// VIP折扣
if (isVip) {
    total = total * 0.9;
}

// 优惠券折扣
if (couponCode != null && !couponCode.isEmpty()) {
    if (couponCode.equals("SAVE10")) {
        total = total * 0.9;
    } else if (couponCode.equals("SAVE20")) {
        total = total * 0.8;
    } else if (couponCode.equals("HALF")) {
        total = total * 0.5;
    }
}
```

**要求**：

使用**策略模式**（Strategy Pattern）重构折扣计算逻辑。

1. **定义策略接口**
   ```java
   public interface DiscountStrategy {
       double applyDiscount(double total);
       boolean isApplicable(String context); // 用于判断策略是否适用
   }
   ```

2. **实现具体策略**
   - `VipDiscountStrategy` - VIP 9折
   - `CouponDiscountStrategy` - 优惠券折扣
   - `NoDiscountStrategy` - 无折扣

3. **使用工厂模式创建策略**（可选）
   ```java
   public class DiscountStrategyFactory {
       public static List<DiscountStrategy> createStrategies(boolean isVip, String couponCode) {
           // 返回适用的策略列表
       }
   }
   ```

4. **修改 OrderProcessor 使用策略**
   - 不再直接计算折扣
   - 委托给策略对象计算

**输入/输出示例**：

```
输入：total = 100, isVip = true, couponCode = "SAVE20"

策略应用顺序：
1. VIP折扣：100 * 0.9 = 90
2. 优惠券折扣：90 * 0.8 = 72

输出：72.0
```

**常见错误**：
- 把策略模式写成带 if-else 的 "策略管理器"
- 策略类之间互相依赖
- 忘记处理策略不存在的边界情况

---

## 进阶作业（选做，+20 分）

### 任务 4：集成静态分析工具

为你的项目配置 SpotBugs 和 PMD，发现并修复潜在问题。

**要求**：

1. **配置 SpotBugs**
   - 在 `pom.xml` 中添加 SpotBugs Maven 插件
   - 运行 `mvn spotbugs:check`
   - 查看报告 `target/spotbugsXml.xml` 或 `target/site/spotbugs.html`

2. **配置 PMD**（可选）
   - 在 `pom.xml` 中添加 PMD Maven 插件
   - 运行 `mvn pmd:check`

3. **修复发现的问题**
   - 至少修复 3 个 SpotBugs 发现的问题
   - 记录修复前后的对比

4. **计算圈复杂度**
   - 使用 PMD 或 IDE 插件计算重构前后的圈复杂度
   - 记录对比数据

**提交物**：
- 修改后的 `pom.xml`
- 静态分析报告截图
- 圈复杂度对比表格

---

### 任务 5：CampusFlow 代码重构

将本周学到的重构手法应用到你的 CampusFlow 项目中。

**要求**：

1. **识别 CampusFlow 中的代码坏味道**
   - 检查 Service 层是否有上帝类
   - 检查是否有重复代码
   - 检查方法是否过长

2. **应用至少 2 种重构手法**
   - 提取方法
   - 移动方法
   - 引入策略模式
   - 其他重构手法

3. **确保测试通过**
   - 重构前运行所有测试，确保通过
   - 重构后再次运行测试，确保行为未改变

4. **编写重构说明**
   - 记录了哪些坏味道
   - 使用了什么重构手法
   - 改进的效果（代码行数、圈复杂度等）

---

## CampusFlow 团队作业（必做，20 分）

### 任务 6：编写 ADR-004

作为本周的首席架构师，编写 ADR-004 记录 CampusFlow 的架构演进决策。

**要求**：

1. **ADR 必须包含以下部分**：
   - 标题和状态
   - 背景（Context）：当前面临的问题
   - 决策（Decision）：你决定怎么做
   - 后果（Consequences）：正面和负面影响
   - 替代方案（Alternatives）：考虑过但拒绝的方案及理由

2. **ADR 主题选择**（任选其一）：
   - 引入策略模式重构业务逻辑
   - 引入静态分析工具作为质量门禁
   - 重构 Service 层以消除上帝类
   - 其他与代码质量相关的架构决策

3. **ADR 质量要求**：
   - 背景描述清晰，让读者理解为什么需要这个决策
   - 决策明确，没有模棱两可的表述
   - 后果分析诚实，包括负面影响
   - 替代方案分析有说服力

**参考模板**：

```markdown
# ADR-004: [决策标题]

## 状态
已接受（2026-02-22）

## 背景
[描述当前面临的问题，引用相关代码坏味道或技术债]

## 决策
我们决定：
1. ...
2. ...

## 后果

### 正面
- ...
- ...

### 负面
- ...
- ...

## 替代方案

### 替代方案 1：[描述]
- 优点：...
- 缺点：...
- 结论：拒绝

### 替代方案 2：[描述]
- 优点：...
- 缺点：...
- 结论：拒绝

## 参考
- Week 02: SOLID 原则
- Week 08: 策略模式与重构
```

---

## AI 协作练习（可选，+10 分附加分）

根据 `shared/ai_progression.md`，Week 07-08 处于"AI 作为重构辅助"阶段——用 AI 建议重构方案，评估是否采纳，并在 ADR 中记录决策。

### 练习：评估 AI 重构建议

下面这段代码是某个 AI 工具为 `OrderProcessor` 生成的重构建议：

```java
public class AIRefactoredOrderProcessor {
    private final OrderRepository repository;

    public AIRefactoredOrderProcessor(OrderRepository repository) {
        this.repository = repository;
    }

    public void processOrder(OrderRequest request) {
        validate(request);
        double total = calculateTotal(request);
        processPayment(request.getPaymentType(), total);
        repository.save(toOrder(request, total));
    }

    private void validate(OrderRequest request) {
        if (request.getCustomerId() == null) {
            throw new IllegalArgumentException("客户ID不能为空");
        }
    }

    private double calculateTotal(OrderRequest request) {
        double total = request.getItems().stream()
            .mapToDouble(this::getItemPrice)
            .sum();

        // 应用折扣
        if (request.isVip()) {
            total *= 0.9;
        }
        if (request.getCouponCode() != null) {
            total = applyCoupon(total, request.getCouponCode());
        }

        // 运费
        total += calculateShipping(request.getShippingMethod(), total);

        return total;
    }

    private double getItemPrice(String item) {
        return switch (item) {
            case "book" -> 50.0;
            case "pen" -> 10.0;
            case "notebook" -> 20.0;
            default -> 30.0;
        };
    }

    private double applyCoupon(double total, String coupon) {
        return switch (coupon) {
            case "SAVE10" -> total * 0.9;
            case "SAVE20" -> total * 0.8;
            case "HALF" -> total * 0.5;
            default -> total;
        };
    }

    private double calculateShipping(String method, double total) {
        return switch (method) {
            case "express" -> total > 200 ? 0 : 20;
            case "standard" -> total > 100 ? 0 : 10;
            case "pickup" -> 0;
            default -> 10;
        };
    }

    private void processPayment(String type, double amount) {
        System.out.println(type + "支付: " + amount);
    }

    private Order toOrder(OrderRequest request, double total) {
        return new Order(request.getCustomerId(), request.getItems(),
                        total, request.getPaymentType());
    }
}
```

**请审查这段 AI 生成的重构代码**：

- [ ] 代码结构是否比原版清晰？
- [ ] 是否还存在代码坏味道？（如长方法、重复代码等）
- [ ] 是否使用了设计模式？使用得当吗？
- [ ] 边界情况处理了吗？（如 null 输入、空列表等）
- [ ] 是否还有改进空间？
- [ ] 你会采纳这个重构方案吗？为什么？

**提交要求**：

1. **审查报告**（至少发现 3 个问题或优点）
2. **决策说明**：如果这是你的项目，你会采纳、部分采纳还是拒绝这个方案？理由是什么？
3. **改进建议**：如果让你基于这个方案继续改进，你会怎么做？
4. **ADR 片段**：用 1-2 段话记录你对 AI 建议的评估决策（可作为 ADR-004 的参考部分）

**提示**：
- AI 生成的代码通常比原版好，但不一定完美
- 关注设计意图，不只是代码格式
- 考虑可扩展性：如果需求变化，这段代码容易修改吗？

---

## 提交要求

### 提交物清单

1. **代码仓库链接**（GitHub/GitLab）
   - 包含重构后的 `OrderProcessor` 及相关类
   - 包含策略模式实现
   - 包含单元测试（测试重构前后的行为一致）

2. **代码坏味道分析报告**（Markdown 格式）
   - 任务 1 的分析结果

3. **重构说明文档**（Markdown 格式）
   - 记录了哪些坏味道
   - 使用了什么重构手法
   - 改进的效果（代码行数、圈复杂度对比）

4. **静态分析报告**（如完成进阶作业）
   - SpotBugs/PMD 配置
   - 分析报告截图
   - 修复的问题列表

5. **ADR-004**（Markdown 格式）
   - CampusFlow 架构演进决策记录

6. **AI 协作练习报告**（如完成）
   - 审查发现的问题清单
   - 决策说明
   - 改进建议

### 提交方式

- 个人作业：通过课程平台提交仓库链接和文档
- 团队作业：在团队仓库中提交，组长汇总提交链接

---

## 评分标准

详见 `RUBRIC.md`

| 维度 | 权重 |
|------|------|
| 代码质量 | 30% |
| 设计模式应用 | 25% |
| 测试覆盖 | 20% |
| ADR 质量 | 15% |
| 工程实践 | 10% |

---

## 常见问题

**Q：重构时测试失败了怎么办？**
A：回滚到上一个通过的版本，然后更小步地重构。重构的黄金法则是：小步前进，频繁测试。

**Q：策略模式是不是过度设计？**
A：如果只有 2-3 种折扣规则，简单的 if-else 也可以。但如果规则会频繁增加或变化，策略模式的价值就体现出来了。在 ADR 中记录你的权衡。

**Q：可以用 AI 生成重构后的代码吗？**
A：可以，但必须人工审查。参考 AI 协作练习——评估 AI 建议的合理性，决定是否采纳，并在 ADR 中记录决策过程。

**Q：CampusFlow 必须重构吗？**
A：是的，这是团队作业的一部分。即使你的代码写得很好，也可以尝试用静态分析工具验证，或者尝试引入策略模式优化某些逻辑。

---

## 参考资源

- 本周章节：`chapters/week_08/CHAPTER.md`
- 示例代码：`chapters/week_08/examples/`
- 如果你遇到困难，可以参考 `starter_code/src/main/java/com/campusflow/App.java` 中的提示
- 《重构：改善既有代码的设计》- Martin Fowler

---

> "重构不是重写，而是让代码在保持行为不变的前提下变得更好。测试是你的安全网，小步前进是你的策略。"
