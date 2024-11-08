package com.example.myapplication;

import android.graphics.Bitmap;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QRCode {
    private String text;
    private Bitmap image;

    public QRCode(String text) {
        this.text = text;
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            this.image = barcodeEncoder.encodeBitmap(this.text, BarcodeFormat.QR_CODE, 256, 256);
        }
        catch (Exception exception) {
            this.text = null;
        }

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

    public Bitmap getImage() {
        if (this.text == null) {
            return null;
        }
        return this.image;

        // TODO implement QR code generation
//        return null; // FIXME temporary since there is no implementation yet
    }
}
