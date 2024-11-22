package com.example.myapplication;

public class FacilityDoesNotExist extends RuntimeException {
    public FacilityDoesNotExist(String message) {
        super(message);
    }
}
