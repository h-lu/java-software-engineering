# Week 12 Starter Code

This starter supports Week 12: integration tests, Mockito-based tests, contract checks, and Bug Bash reporting.

The API implementation is intentionally tiny. It proves that a Javalin server can start, but task API routes are TODO placeholders. Your assignment work is to implement enough behavior to write meaningful integration tests and then document Bug Bash findings.

## Run Commands

```bash
mvn test
mvn compile
java -cp target/classes com.campusflow.App
curl http://localhost:7070/health
curl http://localhost:7070/api/tasks
```

## Files To Edit

- `pom.xml`: add Javalin and Mockito for the real assignment work.
- `src/main/java/com/campusflow/App.java`: replace the placeholder server with Javalin API routes needed by your integration tests.
- `src/main/java/com/campusflow/service/TaskService.java`: add business behavior for task operations.
- `src/main/java/com/campusflow/repository/InMemoryTaskRepository.java`: provide an in-memory test repository.
- `src/test/java/com/campusflow/integration/TaskApiIntegrationTest.java`: create this file for real HTTP tests.
- `src/test/java/com/campusflow/mock/TaskServiceMockTest.java`: create this file for Mockito tests.
- `BUG_BASH_REPORT.md`: document at least two bugs found during Bug Bash.
- `ROOT_CAUSE_ANALYSIS.md`: analyze one bug beyond the surface fix.

## TODO Checklist

- [ ] Start a real Javalin server in integration tests.
- [ ] Test `GET /api/tasks`.
- [ ] Test `POST /api/tasks`.
- [ ] Test at least one error scenario.
- [ ] Add a contract test for expected JSON field names.
- [ ] Add at least two Mockito tests and one Spy test.
- [ ] Complete the Bug Bash report.
- [ ] Complete root cause analysis for one bug.

The included smoke test only verifies server setup. It does not count as the required integration test suite.
