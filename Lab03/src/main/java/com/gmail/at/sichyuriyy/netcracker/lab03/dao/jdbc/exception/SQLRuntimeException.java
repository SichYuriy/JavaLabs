package com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.exception;

/**
 * Created by Yuriy on 3/16/2017.
 */
public class SQLRuntimeException extends RuntimeException {

    public SQLRuntimeException() {
    }

    public SQLRuntimeException(String message) {
        super(message);
    }

    public SQLRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public SQLRuntimeException(Throwable cause) {
        super(cause);
    }
}
