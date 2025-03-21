package com.att.tdp.popcorn_palace.Exceptions;

public class DatabaseInconsistencyException extends RuntimeException {
    public DatabaseInconsistencyException(String message) {
        super(message);
    }
}