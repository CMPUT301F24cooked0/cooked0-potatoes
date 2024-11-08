package com.example.myapplication;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QRCode {
    private String text;
    private Bitmap image;
    private int size;

    public QRCode(String text) {
        this.text = text;
        this.size = 400;

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

    public Bitmap getImage() throws WriterException {
        if (this.text == null) {
            return null;
        }
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        image = barcodeEncoder.encodeBitmap(this.text, BarcodeFormat.QR_CODE, this.size, this.size);
        return image;

    }
}
