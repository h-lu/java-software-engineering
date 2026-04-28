# Week 08 Starter Code：Refactoring and Strategy Pattern

这个 starter 给你一个故意“有味道”的 `OrderProcessor`，以及几个空的 strategy 骨架。你的任务是安全重构，而不是复制一份完成版设计。

## 运行命令

```bash
cd chapters/week_08/starter_code
mvn test
```

当前启用的 smoke test 是 characterization test：它记录重构前的一个已知行为。重构时要让它持续通过，确保外部可见行为没有被你意外改坏。

## 你需要编辑的文件

- `src/main/java/com/campusflow/OrderProcessor.java`
- 按需要提取 `PriceCalculator`, `ShippingCalculator` 或 payment helper 等类。
- `src/main/java/com/campusflow/*DiscountStrategy.java`
- `src/main/java/com/campusflow/DiscountStrategyFactory.java`
- 在 `src/test/java/com/campusflow/` 下新增或启用测试。

## 待办清单

- 在起始 `OrderProcessor` 中识别至少 5 个 code smells。
- 改行为前先补 characterization tests。
- 提取价格、运费、支付、持久化等职责。
- 实现各个 `DiscountStrategy` 类和 factory。
- 启用并补全被禁用的待办测试。
- 在你自己的提交仓库中记录重构决策和 ADR-004。
