package com.vikram.root.multipleimageselector.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by vkramsingh on 29/10/15.
 */
public class ImageUtils {


    public final static int MAX_IMAGE_COUNT_LIMIT = 6;

    public static Bitmap decodeSampledBitmapFromFile(String imagePath,
                                                     int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imagePath, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static boolean CheckValidImage(String fileName) {
        //  MyLog.v("CheckValidImage", fileName);
        if (fileName != null) {
            if (fileName.toLowerCase().endsWith("jpg")) {
                return true;
            }
            if (fileName.toLowerCase().endsWith("jpeg")) {
                return true;
            } else if (fileName.toLowerCase().endsWith("png")) {
                return true;
            } else if (fileName.toLowerCase().endsWith("bmp")) {
                return true;
            } else if (fileName.toLowerCase().endsWith("gif")) {
                return true;
            } else if (fileName.toLowerCase().endsWith("pjpeg")) {
                return true;
            } else if (fileName.startsWith("http://")) {
                return true;
            } else if (fileName.startsWith("https://")) {
                return true;
            } else
                return false;
        } else
            return false;
    }

    public static File getImagepath() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fname = "Image_" + timeStamp + ".jpeg";

        return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), fname);
    }
}
