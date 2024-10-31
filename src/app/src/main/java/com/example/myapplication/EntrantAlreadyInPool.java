package com.example.myapplication;

public class EntrantAlreadyInPool extends RuntimeException {
    public EntrantAlreadyInPool(String message) {
        super(message);
    }
}
