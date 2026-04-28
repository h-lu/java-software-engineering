package edu.campusflow.exception;

public class TaskNotFoundException extends CampusFlowException {

    public TaskNotFoundException(String title) {
        // 待办： 按作业要求完善错误信息。
        super("待办： task not found: " + title);
    }
}
