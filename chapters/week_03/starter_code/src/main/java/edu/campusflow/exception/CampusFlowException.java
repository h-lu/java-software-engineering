package edu.campusflow.exception;

/**
 * CampusFlow 业务异常基类。
 */
public class CampusFlowException extends Exception {

    public CampusFlowException(String message) {
        super(message);
    }

    public CampusFlowException(String message, Throwable cause) {
        super(message, cause);
    }
}
