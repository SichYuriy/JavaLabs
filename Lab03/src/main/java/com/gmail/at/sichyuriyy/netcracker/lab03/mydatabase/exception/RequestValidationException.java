package com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.exception;

/**
 * Created by Yuriy on 11.02.2017.
 */
public class RequestValidationException extends Exception {

    public RequestValidationException() {

    }

    public RequestValidationException(String m) {
        super(m);
    }

    public RequestValidationException(String m, Throwable cause) {
        super(m, cause);

    }
}
