package com.mht.mhtblogservice.exception;

public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    // 可以根据需要扩展错误码
    public BusinessException(String message) {
        super(message);
    }
}
