package edu.campusflow.exception;

public class DuplicateTaskException extends CampusFlowException {

    public DuplicateTaskException(String title) {
        // 待办： 按作业要求完善错误信息。
        super("待办： duplicate task: " + title);
    }
}
