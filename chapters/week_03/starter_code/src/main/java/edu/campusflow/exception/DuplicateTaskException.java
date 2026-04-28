package edu.campusflow.exception;

public class DuplicateTaskException extends CampusFlowException {

    public DuplicateTaskException(String title) {
        // TODO: 按作业要求完善错误信息。
        super("TODO: duplicate task: " + title);
    }
}
