/*
package com.vikram.root.multipleimageselector;

import java.util.ArrayList;
import java.util.HashMap;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Display;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.GridView;

import com.vikram.root.multipleimageselector.Adapters.ImageCursorAdapter;
import com.vikram.root.multipleimageselector.Constants.Constants;
import com.vikram.root.multipleimageselector.Models.ImageModel;


public class MainActivity extends Act implements AbsListView.OnScrollListener{



    public void setupThumbnailsOnGrid(View galleryGrid){

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
       if(scrollState == SCROLL_STATE_IDLE){
           setupThumbnailsOnGrid(view);
       }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    private ImageCursorAdapter mImageCursorAdater;
    private TextView toolbarHeading;
    private android.support.v7.widget.Toolbar mToolbar;
    private int count;
    private ArrayList<ImageModel> mImagesRecievedFromIntentArray;
    private HashMap<String,String> mImagesRecievedFromIntent;
    private ArrayList<Integer> mLocalSelectedPhotosPositions;
    private int fileNameIndex;
    private int fileIdIndex;
    private Cursor mImageCursor;
    private String[] mBucketId;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_layout);
        mToolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarHeading=(TextView)findViewById(R.id.title);
        mBucketId = new String[1];
        mBucketId[0] = getIntent().getStringExtra("bucketId");


        if(mLocalSelectedPhotosPositions == null){
            mLocalSelectedPhotosPositions = new ArrayList<Integer>();
        }
        if(mBucketId !=null){
            final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID, MediaStore.Images.Media.BUCKET_ID };
            final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
            final String selection = MediaStore.Images.Media.BUCKET_ID+"=?";

            mImageCursor =  getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, selection,
                    mBucketId, orderBy + " DESC");

            fileNameIndex = mImageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
            fileIdIndex = mImageCursor.getColumnIndex(MediaStore.Images.Media._ID);

            TextView mOkButton = (TextView)findViewById(R.id.toolbar_ok);


            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);

            mOkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, ReviewSelection.class);
                    intent.putExtra("imageList", getSelectedPhotosHash());
                    intent.putExtra("bucketId", mBucketId);
                    startActivityForResult(intent, Constants.REVIEW_SELECTION);
                }
            });

            mImagesRecievedFromIntent =(HashMap<String,String>) getIntent().getSerializableExtra("imageList");


            mImageCursorAdater = new ImageCursorAdapter(this, mImageCursor,mImagesRecievedFromIntentArray,size,false);

            if(null != mImagesRecievedFromIntent){
                mImageCursorAdater.setMultipleSelectionMode(true);
                count= mImagesRecievedFromIntent.size();
                */
/*makeColorTransition(getResources().getColor(R.color.normalToolbarColor),getResources().getColor(R.color.photoSelected), mToolbar);*//*

                toolbarHeading.setText("Pictures Selected " + count);
                mOkButton.setVisibility(View.VISIBLE);
            }

            GridView gridView = (GridView) findViewById(R.id.imageGrid);
            gridView.setAdapter(mImageCursorAdater);
            gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                    if(!mImageCursorAdater.getMultipleSelectionMode()){
                        mImageCursorAdater.setMultipleSelectionMode(true);
                        */
/*makeColorTransition(getResources().getColor(R.color.normalToolbarColor), getResources().getColor(R.color.photoSelected), mToolbar);*//*

                        toolbarHeading.setText("Pictures Selected " + ++count);
                        RelativeLayout selectionMask = (RelativeLayout) view.findViewById(R.id.chkBox);
                        selectionMask.setVisibility(View.VISIBLE);
                        findViewById(R.id.toolbar_ok).setVisibility(View.VISIBLE);
                        mLocalSelectedPhotosPositions.add(position);

                        return true;
                    }
                    return false;
                }
            });

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (mImageCursorAdater.getMultipleSelectionMode()) {
                        RelativeLayout selectionMask = (RelativeLayout) view.findViewById(R.id.chkBox);
                        boolean isCheck = !mLocalSelectedPhotosPositions.contains(position);
                        selectionMask.setVisibility(isCheck ? View.VISIBLE : View.GONE);
                        if (isCheck) {
                            count++;
                            toolbarHeading.setText("Pictures Selected " + count);
                            mLocalSelectedPhotosPositions.add(position);
                        } else {
                            count--;
                            mLocalSelectedPhotosPositions.remove((Integer) position);
                            if (count == 0) {
                                mImageCursorAdater.setMultipleSelectionMode(false);
                                */
/*makeColorTransition(getResources().getColor(R.color.photoSelected), getResources().getColor(R.color.normalToolbarColor), mToolbar);*//*

                                toolbarHeading.setText("Gallery");
                                findViewById(R.id.toolbar_ok).setVisibility(View.GONE);
                            } else {
                                toolbarHeading.setText("Pictures Selected " + count);
                            }
                        }

                    } else {
                        Toast.makeText(MainActivity.this, "click " + position, Toast.LENGTH_SHORT).show();
                    }

                }
            });

            gridView.setOnScrollListener(this);
        }
    }

    private HashMap<String,String> getSelectedPhotosHash(){
        HashMap<String,String> mPhotos = new HashMap<String,String>();
        for(int position : mLocalSelectedPhotosPositions){
            mImageCursor.moveToPosition(position);
            mPhotos.put(mImageCursor.getString(fileIdIndex), mImageCursor.getString(fileNameIndex));
        }
        if(mImagesRecievedFromIntent != null){
            mPhotos.putAll(mImagesRecievedFromIntent);
        }
        return mPhotos;
    }


    private void makeColorTransition(int colorFrom,int colorTo,View onView ){
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                mToolbar.setBackgroundColor((Integer) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();
    }


    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("imageList",getSelectedPhotosHash());
        returnIntent.putExtra("bucketId",mBucketId);
        setResult(Constants.ALBUM_CONTENTS_BACK_PRESSED,returnIntent);
        super.onBackPressed();
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch(requestCode){
            case Constants.REVIEW_SELECTION:
                if(resultCode == Constants.FINISH_PHOTO_SELECTION){
                    setResult(Constants.FINISH_PHOTO_SELECTION);
                    finish();
                } else if(resultCode == Constants.REVIEW_SELECTION_BACK_PRESSED){
                    mImagesRecievedFromIntent = (HashMap<String,String>) data.getSerializableExtra("imageList");
                    mLocalSelectedPhotosPositions.clear();
                }
                break;
        }
    }
}*/
