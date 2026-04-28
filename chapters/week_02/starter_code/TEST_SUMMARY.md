# Week 02 Starter 测试说明

本 starter 只包含少量烟雾测试，用来确认 Maven 项目、包名和骨架类能正常编译。

当前测试文件：

- `StarterSmokeTest.java`：验证 `Book`、`BookCollection` 等骨架可实例化，并保留 TODO 占位。

完成作业后，请自行添加至少 2 个真实测试，例如：

- `Book` 拒绝负数价格。
- `Book` 拒绝未来出版年份。
- `BookCollection.totalPrice()` 能正确累计价格。
- `BookCollection.filterByAuthor()` 只返回目标作者的图书。

运行命令：

```bash
mvn test
```
