package com.example.myapplication;

import android.graphics.Bitmap;

/*
This class is responsible for creating a QR code and setting its text. It also returns the
generated QR code image.
 */
public class QRCode {
    private String text;

    /**
     * create a QR code and set the text
     * @param text
     */
    public QRCode(String text) {
        this.text = text;
    }

    /**
     * create a QR code with no text - set the text later
     */
    public QRCode() { // if you want to set the text later
        this.text = null;
    }

    /**
     * set the QR code text (will modify the generated image)
     * @param text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * get the text currently set
     * @return
     */
    public String getText() {
        return this.text;
    }

    /**
     * generate the image of the QR code
     * @return
     */
    public Bitmap getImage() {
        if (this.text == null) {
            return null;
        }
        // TODO implement QR code generation
        return null; // FIXME temporary since there is no implementation yet
    }
}