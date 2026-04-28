package edu.campusflow.exception;

public class TaskNotFoundException extends CampusFlowException {

    public TaskNotFoundException(String title) {
        // TODO: 按作业要求完善错误信息。
        super("TODO: task not found: " + title);
    }
}
