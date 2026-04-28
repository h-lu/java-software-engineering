package edu.campusflow;

import edu.campusflow.domain.Task;
import edu.campusflow.exception.DuplicateTaskException;
import edu.campusflow.exception.InvalidTaskDataException;
import edu.campusflow.exception.TaskNotFoundException;
import edu.campusflow.util.TaskFileLoader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StarterSmokeTest {

    @Test
    void skeletonClassesCompile() {
        assertDoesNotThrow(Task::new);
        assertDoesNotThrow(TaskFileLoader::new);
    }

    @Test
    void customExceptionsCarryTodoMessages() {
        Exception invalid = new InvalidTaskDataException("title", "required");
        Exception duplicate = new DuplicateTaskException("买牛奶");
        Exception notFound = new TaskNotFoundException("买牛奶");

        assertTrue(invalid.getMessage().contains("TODO"));
        assertTrue(duplicate.getMessage().contains("TODO"));
        assertTrue(notFound.getMessage().contains("TODO"));
    }
}
