package com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.exception;

/**
 * Created by Yuriy on 29.01.2017.
 */
public class ConstraintException extends DatabaseException {

    public ConstraintException() {

    }

    public ConstraintException(String m) {
        super(m);
    }

    public ConstraintException(String m, Throwable cause) {
        super(m, cause);
    }

}
