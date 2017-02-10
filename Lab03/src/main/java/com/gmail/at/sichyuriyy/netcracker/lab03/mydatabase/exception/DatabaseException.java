package com.gmail.at.sichyuriyy.netcracker.lab03.mydatabase.exception;

/**
 * Created by Yuriy on 29.01.2017.
 */
public class DatabaseException extends RuntimeException {


    public DatabaseException() {

    }

    public DatabaseException(String m) {
        super(m);
    }

    public DatabaseException(String m, Throwable cause) {
        super(m, cause);

    }
}
