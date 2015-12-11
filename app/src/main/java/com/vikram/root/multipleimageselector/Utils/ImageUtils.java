package com.vikram.root.multipleimageselector.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by vkramsingh on 29/10/15.
 */
public class ImageUtils {


    public final static int MAX_IMAGE_COUNT_LIMIT = 6;


    public static Bitmap decodeSampledBitmapFromFileWithOrientation(String imagePath,int reqWidth, int reqHeight,int orientation){
        Bitmap scaledBitmap = decodeSampledBitmapFromFile(imagePath,reqWidth,reqHeight);
        if(orientation !=0){
            return rotateBitmap(scaledBitmap,orientation);
        }
        else{
            return scaledBitmap;
        }

    }

    public static void galleryAddPic(File newImageFile,Context context) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        if (newImageFile != null) {
            File f = new File(newImageFile.getPath());
            if (f != null) {
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                context.sendBroadcast(mediaScanIntent);

            }
        }
    }


    public static void callBroadCastToUpdateLibrary(Context context) {
        if (Build.VERSION.SDK_INT >= 14) {
            Log.e("-->", " >= 14");
            MediaScannerConnection.scanFile(context, new String[]{Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                /*
                 *   (non-Javadoc)
                 * @see android.media.MediaScannerConnection.OnScanCompletedListener#onScanCompleted(java.lang.String, android.net.Uri)
                 */
                public void onScanCompleted(String path, Uri uri) {
                    Log.e("ExternalStorage", "Scanned " + path + ":");
                    Log.e("ExternalStorage", "-> uri=" + uri);
                }
            });
        } else {
            Log.e("-->", " < 14");
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        }
    }



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



    public static int getExifOrientation(String filepath) {
        int degree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
        } catch (IOException ex) {
            Log.e(ImageUtils.class.getSimpleName(), "EXIF orientation could not be obtained");
        }
        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }

            }
        }
        return degree;
    }


    public static Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.setRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}
