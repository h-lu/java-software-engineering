# Week 05 Starter Code：Collections and Generics

这是 Week 05 作业的独立 Maven 起步项目，主题是 Collections and Generics。项目故意留了一些未完成位置：看到 `TODO` 或“待办”时，就是你需要补上的工作。

## 运行命令

```bash
cd chapters/week_05/starter_code
mvn test
```

提供的 smoke tests 只确认项目结构和 Maven 配置能跑通，不能证明作业已经完成。

## 你需要编辑的文件

- `src/main/java/edu/campusflow/library/LibraryTracker.java`
- `src/main/java/edu/campusflow/util/CollectionUtils.java`
- 在 `src/test/java/edu/campusflow/` 下补充你自己的测试

## 待办清单

- 完成 `addBook`, `findBook`, `listAllBooks`, `borrowBook`, `getBorrowRecordsByUser`, `returnBook`, `removeBook`。
- 使用 `HashMap<String, Book>` 按 ISBN 快速查找图书，使用 `ArrayList<BorrowRecord>` 保存借阅历史。
- 返回集合副本，不要把内部状态直接暴露给外部代码。
- 用 generics 实现 `CollectionUtils.groupBy`, `filter`, `findFirst`。
- 补充边界测试：null 输入、重复 ISBN、缺失记录，以及返回列表副本是否安全。
