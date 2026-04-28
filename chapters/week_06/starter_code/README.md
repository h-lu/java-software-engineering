# Week 06 Starter Code: JUnit 5 Test Skeleton

This starter is for practicing JUnit 5 against a working `LibraryTracker`. The production code is intentionally small so you can focus on test design.

## 目录结构

```text
starter_code/
├── pom.xml
├── src/main/java/com/campusflow/
│   ├── Book.java
│   └── LibraryTracker.java
└── src/test/java/com/campusflow/
    └── LibraryTrackerTest.java
```

## Run

```bash
cd chapters/week_06/starter_code
mvn test
```

The initial test suite passes because only one smoke test is active. Disabled tests are TODO templates for you to complete and enable.

## Files To Edit

- `src/test/java/com/campusflow/LibraryTrackerTest.java`
- Add more test classes under `src/test/java/com/campusflow/` if that keeps your tests clearer.
- Read `src/main/java/com/campusflow/LibraryTracker.java`, but do not weaken business behavior just to make a test pass.

## TODO Checklist

- Use `@BeforeEach` for fresh test data.
- Cover `addBook`, `findByIsbn`, `listAllBooks`, `removeBook`, `borrowBook`, `returnBook`, and `hasBorrowRecord`.
- Use `assertThrows` for invalid input and missing records.
- Add at least one `@ParameterizedTest`.
- Optional: run coverage after you add real tests. JaCoCo writes reports to `target/site/jacoco/index.html`.
