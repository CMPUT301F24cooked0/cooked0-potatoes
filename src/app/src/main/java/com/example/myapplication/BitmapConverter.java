package com.example.myapplication;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class BitmapConverter {
    public static Bitmap StringToBitmap(String imageString) {
        // TODO temporary
        return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
    }

    /**
     * Converts a Bitmap to a String.
     * Lowers the resolution of the Bitmap if necessary until the String is <= maximumStringLength
     * @param imageBitmap
     * @param maximumStringLength
     * @return
     * @throws Exception if maximumStringLength is <= 8
     */
    public static String BitmapToCompressedString(Bitmap imageBitmap, int maximumStringLength) throws Exception {
        if (maximumStringLength <= 8) {
            throw new Exception("cannot compress Bitmap to a String of length <= 8");
        }
        // base64 is approximately 4/3 times the size of the original bytes
        String encodedImage = BitmapToString(imageBitmap);
        int imageStringLength = encodedImage.length();
        // create copy of imageBitmap so that we can recompress the original on successive attempts for better quality
        Bitmap compressedImageBitmap = imageBitmap.copy(imageBitmap.getConfig(), imageBitmap.isMutable());
        int scalingFactor = 1;
        int width = imageBitmap.getWidth();
        int height = imageBitmap.getHeight();
        while (imageStringLength > maximumStringLength) {
            // recompress original image with increasing powers of 2 scaling factors
            scalingFactor *= 2;
            compressedImageBitmap = Bitmap.createScaledBitmap(imageBitmap, width/scalingFactor, height/scalingFactor, true);
            encodedImage = BitmapToString(compressedImageBitmap);
            imageStringLength = encodedImage.length();
        }

        return encodedImage;
    }

    /**
     * Converts a Bitmap to a String
     * @param imageBitmap
     * @return
     */
    public static String BitmapToString(Bitmap imageBitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

        return encodedImage;
    }
}
