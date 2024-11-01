package com.example.myapplication;

public class EventAlreadyExistsAtFacility extends RuntimeException {
    public EventAlreadyExistsAtFacility(String message) {
        super(message);
    }
}
