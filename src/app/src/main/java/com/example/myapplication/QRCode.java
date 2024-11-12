package com.example.myapplication;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

/*
This class is responsible for creating a QR code and setting its text. It also returns the
generated QR code image.
 */
public class QRCode {
    private String text;
    private Bitmap image;
    private int size;

    /**
     * create a QR code and set the text
     * @param text
     */
    public QRCode(String text) {
        this.text = text;
        this.size = 400;

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
    public Bitmap getImage() throws WriterException {
        if (this.text == null) {
            return null;
        }
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        image = barcodeEncoder.encodeBitmap(this.text, BarcodeFormat.QR_CODE, this.size, this.size);
        return image;

    }
}