/*
package com.vikram.root.multipleimageselector;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;

@SuppressWarnings({ "deprecation", "unused" })
public class Cropper extends Activity {

    public FrameLayout board;
    public View part1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        board = new FrameLayout(this);
        board = (FrameLayout) findViewById(R.id.Board);
        part1 = new View(this);
        part1 = findViewById(R.id.part1);
        try {

            Paint paint = new Paint();
            paint.setFilterBitmap(true);
            Bitmap bitmapOrg = BitmapFactory.decodeResource(getResources(), R.drawable.);

            int targetWidth = 300;
            int targetHeight = 300;


            Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);

            RectF rectf = new RectF(0, 0, 100, 100);

            Canvas canvas = new Canvas(targetBitmap);
            Path path = new Path();

            path.addRect(rectf, Path.Direction.CW);
            canvas.clipPath(path);

            canvas.drawBitmap(bitmapOrg, new Rect(0, 0, bitmapOrg.getWidth(), bitmapOrg.getHeight()),
                    new Rect(0, 0, targetWidth, targetHeight), paint);


            Matrix matrix = new Matrix();
            matrix.postScale(1f, 1f);
            Bitmap resizedBitmap = Bitmap.createBitmap(targetBitmap, 0, 0, 100, 100, matrix, true);

            */
/*convert Bitmap to resource *//*

            BitmapDrawable bd = new BitmapDrawable(resizedBitmap);

            part1.setBackgroundDrawable(bd);

        } catch (Exception e) {
            System.out.println("Error1 : " + e.getMessage() + e.toString());
        }

    }
}*/
