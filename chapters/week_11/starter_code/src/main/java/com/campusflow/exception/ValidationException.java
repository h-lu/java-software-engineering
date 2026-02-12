/*
 * ValidationException - 请求验证失败异常。
 *
 * 当请求数据验证失败时抛出，对应 HTTP 400 状态码。
 */
package com.campusflow.exception;

/**
 * 验证失败异常。
 */
public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String field, String message) {
        super(field + ": " + message);
    }
}
