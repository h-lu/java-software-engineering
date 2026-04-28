package edu.campusflow.exception;

public class InvalidTaskDataException extends CampusFlowException {

    public InvalidTaskDataException(String field, String reason) {
        // 待办： 输出类似 "任务数据无效 [title]: 标题不能为空" 的消息。
        super("待办： invalid task data [" + field + "]: " + reason);
    }
}
