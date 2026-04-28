# Week 05 Starter Code: Collections and Generics

This starter is a small Maven project for the Week 05 assignment. It is intentionally incomplete: the `TODO` markers are the work you should finish.

## Run

```bash
cd chapters/week_05/starter_code
mvn test
```

The provided smoke tests only verify that the project is wired correctly. They do not prove the assignment is complete.

## Files To Edit

- `src/main/java/LibraryTracker.java`
- `src/main/java/CollectionUtils.java`
- Add your own tests under `src/test/java/`

## TODO Checklist

- Complete `addBook`, `findBook`, `listAllBooks`, `borrowBook`, `getBorrowRecordsByUser`, `returnBook`, and `removeBook`.
- Use `HashMap<String, Book>` for ISBN lookup and `ArrayList<BorrowRecord>` for borrow history.
- Return collection copies instead of exposing internal state.
- Implement `CollectionUtils.groupBy`, `filter`, and `findFirst` with generics.
- Add boundary tests for null input, duplicate ISBN, missing records, and returned-list copies.
