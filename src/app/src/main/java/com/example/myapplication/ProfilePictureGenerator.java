package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class ProfilePictureGenerator {

    /**
     * Generates a profile image based on the first letter of the user's name.
     * @author Daniyal Abbas, Ishaan Chandel
     * @param name - String storing name of the user to perform profile picture generation with
     * @return
     */
    public static Bitmap generateProfileImage(String name) {
        String firstNameInitial = !name.isEmpty() ? name.substring(0, 1).toUpperCase() : "A";

        int size = 1024;
        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // Draw a circle
        Paint paint = new Paint();
        paint.setColor(Color.LTGRAY);  // Set background color
        paint.setAntiAlias(true);
        canvas.drawCircle(size / 2, size / 2, size / 2, paint);

        paint.setColor(Color.WHITE);
        paint.setTextSize(size/2);
        paint.setTextAlign(Paint.Align.CENTER);

        Rect textBounds = new Rect();
        paint.getTextBounds(firstNameInitial, 0, firstNameInitial.length(), textBounds);
        int x = size / 2;
        int y = size / 2 + (textBounds.height() / 2);

        canvas.drawText(firstNameInitial, x, y, paint);

        return bitmap;
    }
}
