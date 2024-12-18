package com.example.myapplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import android.graphics.Bitmap;

import com.google.zxing.WriterException;

import org.junit.Test;

public class QRCodeUnitTest {
    @Test
    public void firstConstructorTest() {
        QRCode qrCode = new QRCode("text");
    }

    @Test
    public void secondConstructorTest() {
        QRCode qrCode = new QRCode();
    }

    @Test
    public void setGetTextTest() {
        QRCode qrCode = new QRCode();
        assertNull(qrCode.getText());
        String text = "text";
        qrCode.setText(text);
        assertEquals(qrCode.getText(), text);
    }

    @Test
    public void getImageTest() {
        // TODO add test for getimage method


    }
}
