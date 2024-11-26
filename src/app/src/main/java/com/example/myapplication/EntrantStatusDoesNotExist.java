package com.example.myapplication;

public class EntrantStatusDoesNotExist extends RuntimeException {
    public EntrantStatusDoesNotExist(String message) {
        super(message);
    }
}
