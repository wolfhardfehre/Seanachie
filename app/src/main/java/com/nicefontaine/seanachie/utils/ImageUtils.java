package com.nicefontaine.seanachie.utils;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.IOException;

import static android.media.ExifInterface.ORIENTATION_ROTATE_180;
import static android.media.ExifInterface.ORIENTATION_ROTATE_270;
import static android.media.ExifInterface.ORIENTATION_ROTATE_90;
import static android.media.ExifInterface.ORIENTATION_UNDEFINED;
import static android.media.ExifInterface.TAG_ORIENTATION;


public class ImageUtils {

    private ImageUtils() {}

    public static Bitmap loadImage(String path, int width) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions = computeOptions(path, width, bmOptions);
        return BitmapFactory.decodeFile(path, bmOptions);
    }

    private static BitmapFactory.Options computeOptions(String path, int width, BitmapFactory.Options bmOptions) {
        int imageWidth = getImageWidth(path, bmOptions);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = imageWidth / width;
        bmOptions.inPurgeable = true;
        return bmOptions;
    }

    private static int getImageWidth(String path, BitmapFactory.Options bmOptions) {
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bmOptions);
        return bmOptions.outWidth;
    }

    public static Bitmap rotateImage(Bitmap source, String path) throws IOException {
        float angle = getRotationAngle(path);
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    private static float getRotationAngle(String path) throws IOException {
        ExifInterface exifInterface = new ExifInterface(path);
        int orientation = exifInterface.getAttributeInt(TAG_ORIENTATION, ORIENTATION_UNDEFINED);
        switch(orientation) {
            case ORIENTATION_ROTATE_90:
                return 90;
            case ORIENTATION_ROTATE_180:
                return 180;
            case ORIENTATION_ROTATE_270:
                return 270;
            default:
                return 0;
        }
    }
}
