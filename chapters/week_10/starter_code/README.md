# Week 10 Starter Code

This starter supports the Week 10 assignment: generate a CampusFlow frontend with AI, review it, fix it, and wire it to the backend with CORS.

It is not a finished frontend or a finished API. The Java server only proves that Maven and Javalin are wired correctly. Your main work is in the Markdown reports and the files under `frontend/`.

## Run Commands

```bash
mvn test
mvn compile
java -cp target/classes com.campusflow.App
curl http://localhost:7070/health
```

To test the frontend manually, start your Week 09 backend or extend this starter, then open `frontend/index.html` in a browser.

## Files To Edit

- `PROMPT.md`: write the prompt you used to generate the first frontend.
- `AI_TOOL.md`: record the AI tool and model version.
- `frontend/ai_generated.html`: save the original AI output without edits.
- `REVIEW.md`: complete the checklist and record at least three issue categories.
- `frontend/index.html`: build your reviewed and fixed frontend.
- `pom.xml`: add Javalin if you use this starter backend for the CORS task.
- `src/main/java/com/campusflow/App.java`: replace the placeholder server with the CORS configuration needed for browser calls.

## TODO Checklist

- [ ] Write a prompt with role, task, constraints, and output format.
- [ ] Save the unmodified AI generated frontend.
- [ ] Review for XSS, empty states, loading/error states, and hallucinated APIs.
- [ ] Fix the reviewed frontend without using unsafe `innerHTML` for user content.
- [ ] Add at least two UX improvements.
- [ ] Configure and verify CORS with the backend you will use for demo.
- [ ] Capture the required screenshots and notes for submission.

The smoke test intentionally checks only starter wiring. Add browser or API tests after you complete your implementation.
