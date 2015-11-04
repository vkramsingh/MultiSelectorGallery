/*
package com.vikram.root.multipleimageselector;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vikram.root.multipleimageselector.Constants.Constants;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class ReviewSelection extends Activity implements View.OnClickListener {

    private  ImageView mImageBig;
    private HashMap<String,String> mSelectedImagesHash;
    Iterator mSelectedPhotosHashIterator;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_selection);

        mImageBig =(ImageView)findViewById(R.id.imageBig);

        mSelectedImagesHash = (HashMap<String,String>)getIntent().getSerializableExtra("imageList");
        if(mSelectedImagesHash !=null){
            mSelectedPhotosHashIterator = mSelectedImagesHash.entrySet().iterator();
        }

        if(mSelectedPhotosHashIterator.hasNext()){
            mImageBig.setImageBitmap(decodeSampledBitmapFromFile((String) ((Map.Entry) mSelectedPhotosHashIterator.next()).getValue(), mImageBig.getLayoutParams().width, mImageBig.getLayoutParams().height));
        }
        LinearLayout mImageLayout=(LinearLayout)findViewById(R.id.mImageLayout);
        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        findViewById(R.id.photo_tag).setOnClickListener(this);
        if(mSelectedImagesHash.size()<=6){
            mSelectedPhotosHashIterator = mSelectedImagesHash.entrySet().iterator();
            while(mSelectedPhotosHashIterator.hasNext()){
                View mThumbnail = li.inflate(R.layout.image_thumbnail,null,false);
                String mPhotoPath = (String)((Map.Entry)mSelectedPhotosHashIterator.next()).getValue();
                ImageView mImageView=(ImageView)mThumbnail.findViewById(R.id.image_thumbnail_imageview);
                mImageView.setImageBitmap(decodeSampledBitmapFromFile(mPhotoPath, mImageView.getLayoutParams().width, mImageView.getLayoutParams().height));
                mImageLayout.addView(mThumbnail);
                mImageView.setOnClickListener(this);
                mImageView.setTag(mPhotoPath);

            }
            View mAddThumbnail=li.inflate(R.layout.image_add,null,false);
            mImageLayout.addView(mAddThumbnail);
            findViewById(R.id.image_thumbnail_add_photos).setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_thumbnail_imageview:
                mImageBig.setImageBitmap(decodeSampledBitmapFromFile((String)v.getTag(), mImageBig.getLayoutParams().width, mImageBig.getLayoutParams().height));
                break;
            case R.id.image_thumbnail_add_photos:
                String bucketId=getIntent().getStringExtra("bucketId");
                Intent intent = new Intent(ReviewSelection.this,AlbumContents.class);
                intent.putExtra("isShow", true);
                intent.putExtra("bucketId",bucketId);
                intent.putExtra("imageList", mSelectedImagesHash);
                startActivity(intent);
                break;
            case R.id.photo_tag:
                getBasicDialog(this,"Select Tag").show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("imageList", mSelectedImagesHash);
        setResult(Constants.REVIEW_SELECTION_BACK_PRESSED, returnIntent);
        finish();
    }

    public static Dialog getBasicDialog(Context context, String dialogTitle) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_dialog_title_bar);
        dialog.setContentView(R.layout.custom_dialog_for_ppf);
        //dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialog;
        TextView dialogTitleTV = (TextView) dialog.findViewById(R.id.dialogTitle);
        final TextView dialogDone = (TextView) dialog.findViewById(R.id.done);
        dialogTitleTV.setText(dialogTitle);
        dialogDone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        return dialog;
    }
}*/
