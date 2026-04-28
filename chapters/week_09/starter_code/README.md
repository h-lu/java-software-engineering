# Week 09 Starter Code

This is a small TODO skeleton for the Week 09 assignment: convert CampusFlow from a CLI program into a Javalin REST API.

## What Is Included

- A Java 21 Maven project with JUnit 5.
- A runnable `App` with `GET /health`.
- Placeholder task routes that return `501 Not Implemented`.
- Minimal domain, repository, service, controller, and exception classes with TODO markers.
- Smoke tests that verify the starter project compiles and the server wiring works.
- TODO comments showing where to add Javalin 6.x and Jackson for the real implementation.

## Run Commands

```bash
mvn test
mvn compile
java -cp target/classes com.campusflow.App
curl http://localhost:7070/health
curl http://localhost:7070/tasks
```

## Files To Edit

- `pom.xml`: add Javalin 6.x and Jackson.
- `src/main/java/com/campusflow/App.java`: replace the placeholder server with Javalin routes and global error handlers.
- `src/main/java/com/campusflow/controller/TaskController.java`: translate HTTP requests into service calls.
- `src/main/java/com/campusflow/service/TaskService.java`: implement validation and business operations.
- `src/main/java/com/campusflow/repository/InMemoryTaskRepository.java`: implement in-memory persistence.
- `src/main/java/com/campusflow/model/Task.java`: add fields or behavior needed by your Week 08 model.
- `docs/ADR-003.md`: create this file in your submission and explain your REST API decisions.
- `docs/API_TESTING.md`: create this file and record your curl or HTTP client tests.

## TODO Checklist

- [ ] Implement `GET /tasks`.
- [ ] Implement `GET /tasks/{id}` with `404` for missing tasks.
- [ ] Implement `POST /tasks` with request validation and `201`.
- [ ] Implement `PUT /tasks/{id}`.
- [ ] Implement `DELETE /tasks/{id}` with `204`.
- [ ] Return a standard JSON error body for validation and not-found errors.
- [ ] Add meaningful unit or integration tests for your completed routes.
- [ ] Write ADR-003 and API testing notes.

The starter tests are intentionally smoke tests only. Replace or extend them as you implement the assignment.
