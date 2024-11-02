package com.example.myapplication;

import android.media.Image;

public class QRCode {
    private String text;

    public QRCode(String text) {
        this.text = text;
    }

    public QRCode() { // if you want to set the text later
        this.text = null;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public Image getImage() {
        if (this.text == null) {
            return null;
        }
        // TODO implement QR code generation
        return null; // FIXME temporary since there is no implementation yet
    }
}
