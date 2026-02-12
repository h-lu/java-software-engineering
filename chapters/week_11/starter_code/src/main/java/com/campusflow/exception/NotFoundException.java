/*
 * NotFoundException - 资源未找到异常。
 *
 * 当请求的资源不存在时抛出，对应 HTTP 404 状态码。
 */
package com.campusflow.exception;

/**
 * 资源未找到异常。
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String resourceType, String id) {
        super(resourceType + " not found: " + id);
    }
}
