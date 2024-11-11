package com.example.myapplication;

public class EventDoesNotExist extends RuntimeException {
    public EventDoesNotExist(String message) {
        super(message);
    }
}
