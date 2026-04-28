# Week 11 Starter Code

This starter is for the Week 11 quality gate assignment. It intentionally does not include finished SpotBugs or JaCoCo configuration. Students should add those plugins, run the tools, analyze the results, and improve the tests.

## Run Commands

```bash
mvn test
```

After you add the required plugins:

```bash
mvn spotbugs:check
mvn test jacoco:report
```

## Files To Edit

- `pom.xml`: add SpotBugs and JaCoCo plugin configuration.
- `src/main/java/com/campusflow/quality/CodeWithIssues.java`: inspect and fix at least three real issues after running SpotBugs.
- `src/main/java/com/campusflow/quality/TaskStatusCalculator.java`: use JaCoCo to identify missing branches and add tests.
- `src/test/java/com/campusflow/quality/TaskStatusCalculatorTest.java`: extend coverage based on the report.
- `TECHNICAL_DEBT.md`: replace the starter rows with your own prioritized backlog.
- `TEST_SUMMARY.md`: record SpotBugs and JaCoCo commands, findings, and before/after results.

## TODO Checklist

- [ ] Configure SpotBugs in `pom.xml`.
- [ ] Run SpotBugs and save the output as `spotbugs_report.txt`.
- [ ] Fix at least three high or medium priority issues.
- [ ] Configure JaCoCo in `pom.xml`.
- [ ] Generate and inspect the coverage report.
- [ ] Add at least three tests for uncovered branches.
- [ ] Update `TECHNICAL_DEBT.md` with at least ten prioritized items.
- [ ] Define your quality gate thresholds and explain the tradeoffs.

The included test is only a smoke test. It is not enough for the assignment coverage target.
