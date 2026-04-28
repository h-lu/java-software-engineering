# Week 07 Starter Code：JDBC Persistence

这是 Week 07 JDBC 作业的独立 Maven 起步项目。它是待办骨架，不是参考答案。

## 运行命令

```bash
cd chapters/week_07/starter_code
mvn test
```

提供的测试只是检查 model 和 Maven 配置能跑通。实现 JDBC 部分时，请同步补充 repository 测试。

## 你需要编辑的文件

- `src/main/java/edu/campusflow/config/DatabaseConfig.java`
- `src/main/java/edu/campusflow/config/DatabaseInitializer.java`
- `src/main/java/edu/campusflow/repository/JdbcTaskRepository.java`
- `src/main/resources/schema.sql`
- 按 `ASSIGNMENT.md` 增加 SQLite runtime driver 和 H2 test dependency 时，需要编辑 `pom.xml`
- 新增 `src/test/java/edu/campusflow/repository/JdbcTaskRepositoryTest.java`

## 待办清单

- 在 `schema.sql` 中创建 `tasks` table。
- 实现 `DatabaseConfig.getConnection()`。
- 用 try-with-resources 实现 `DatabaseInitializer.initialize()`。
- 实现 `JdbcTaskRepository.save`, `findById`, `findAll`, `delete`, `findByStatus`。
- 使用 `PreparedStatement`；不要把用户输入拼接进 SQL 字符串。
- 跑真实 SQLite/H2 repository 测试前，先补齐 JDBC driver dependencies。
- 使用 H2 memory database 覆盖 CRUD、缺失行、非法参数和排序等场景。
