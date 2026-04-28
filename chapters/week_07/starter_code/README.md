# Week 07 Starter Code: JDBC Persistence

This starter is an independent Maven project for the Week 07 JDBC assignment. It is a TODO skeleton, not a reference solution.

## Run

```bash
cd chapters/week_07/starter_code
mvn test
```

The provided tests are smoke tests for the model and Maven setup. Add repository tests as you implement the JDBC pieces.

## Files To Edit

- `src/main/java/edu/campusflow/config/DatabaseConfig.java`
- `src/main/java/edu/campusflow/config/DatabaseInitializer.java`
- `src/main/java/edu/campusflow/repository/JdbcTaskRepository.java`
- `src/main/resources/schema.sql`
- `pom.xml` when you add the SQLite runtime driver and H2 test dependency from `ASSIGNMENT.md`
- Add `src/test/java/edu/campusflow/repository/JdbcTaskRepositoryTest.java`

## TODO Checklist

- Create the `tasks` table in `schema.sql`.
- Implement `DatabaseConfig.getConnection()`.
- Implement `DatabaseInitializer.initialize()` with try-with-resources.
- Implement `JdbcTaskRepository.save`, `findById`, `findAll`, `delete`, and `findByStatus`.
- Use `PreparedStatement`; do not build SQL by concatenating user input.
- Add the JDBC driver dependencies before running real SQLite/H2 repository tests.
- Use H2 memory database tests for CRUD, missing rows, invalid arguments, and ordering.
