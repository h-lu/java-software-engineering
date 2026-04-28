package edu.campusflow.exception;

public class InvalidTaskDataException extends CampusFlowException {

    public InvalidTaskDataException(String field, String reason) {
        // TODO: 输出类似 "任务数据无效 [title]: 标题不能为空" 的消息。
        super("TODO: invalid task data [" + field + "]: " + reason);
    }
}
