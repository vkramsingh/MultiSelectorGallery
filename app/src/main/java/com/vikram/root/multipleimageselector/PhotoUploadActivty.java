package com.vikram.root.multipleimageselector;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;

import android.os.Bundle;


import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.vikram.root.multipleimageselector.Fragments.AlbumContentsFragment;
import com.vikram.root.multipleimageselector.Fragments.AlbumFragment;
import com.vikram.root.multipleimageselector.Fragments.ReviewSelectionFragment;
import com.vikram.root.multipleimageselector.Models.ImageModel;

import java.util.ArrayList;


public class PhotoUploadActivty extends FragmentActivity implements AlbumFragment.OnAlbumSelectedListener,View.OnClickListener,ReviewSelectionFragment.AddMoreImagesSelected{

    private ArrayList<String> mSelectedAlbums;
    public ArrayList<ImageModel> mSelectedImagesArray;
/*
    public HashMap<String,String> mSelectedImages;
*/
    private BackPressedCallback backPressedCallbackListener;
    private String mLastBucketId;
    private View mToolbar;
    private TextView mOkButton;
    private ImageView mDeleteButton;
    private TextView mToolbarHeading;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Log.d("@vikram", "Activity Created");

        setContentView(R.layout.photo_upload_activity);
        if(savedInstanceState ==null){
            setFragment();
        }
        mToolbar = findViewById(R.id.tool_bar);
        mOkButton = (TextView)mToolbar.findViewById(R.id.toolbar_ok);
        mToolbarHeading = (TextView)mToolbar.findViewById(R.id.title);
        mDeleteButton = (ImageView)mToolbar.findViewById(R.id.toolbar_delete);
        /*findViewById(R.id.multiselect_on).setOnClickListener(this);*/
        mOkButton.setOnClickListener(this);
        mSelectedImagesArray = new ArrayList<ImageModel>();
        /*mSelectedImages = new HashMap<String,String>();*/
        ImageView mBackImage = (ImageView)mToolbar.findViewById(R.id.back);
        mBackImage.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("@vikram", "Activity Started");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("@vikram", "Activity resumed");
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d("@vikram", "Activity Paused");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d("@vikram", "Activity Stopped");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.d("@vikram", "Activity Restarted");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d("@vikram", "Activity Destroyed");
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.toolbar_ok:
                ReviewSelectionFragment reviewSelection = new ReviewSelectionFragment();
                if(backPressedCallbackListener !=null){
                    mSelectedImagesArray = backPressedCallbackListener.backPressed();
                    /*mSelectedImages = backPressedCallbackListener.backPressed();*/
                }


                reviewSelection.setSelectedImages(mSelectedImagesArray);

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                transaction.replace(R.id.fragment_container, reviewSelection);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            /*case R.id.multiselect_on:
                if(backPressedCallbackListener!=null){
                    backPressedCallbackListener.multiSelectOn();
                }*/
            case R.id.back:
                onBackPressed();
                break;

        }
    }

    @Override
    public void addMoreImagesSelected() {

        AlbumContentsFragment contentsFragment = AlbumContentsFragment.newInstance(mLastBucketId);

        contentsFragment.setSelectedImages(mSelectedImagesArray);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, contentsFragment);
        getSupportFragmentManager().popBackStack();
        backPressedCallbackListener=contentsFragment;
        transaction.commit();

    }


    public interface BackPressedCallback{
        ArrayList<ImageModel> backPressed();
/*
        void multiSelectOn();
*/
    }

    private void setFragment(){

        AlbumFragment albumFragment = AlbumFragment.newInstance(mSelectedAlbums);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.add(R.id.fragment_container, albumFragment);
        transaction.commit();
    }


    @Override
    public void onAlbumSelected(String bucketId) {

        mLastBucketId = bucketId;

        AlbumContentsFragment newFragment = AlbumContentsFragment.newInstance(bucketId);
        newFragment.setSelectedImages(mSelectedImagesArray);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();



        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);
        // Commit the transaction
        transaction.commit();

        if(newFragment instanceof BackPressedCallback){
            backPressedCallbackListener = (BackPressedCallback)newFragment;
        }
    }

    @Override
    public void onBackPressed() {

        mDeleteButton.setVisibility(View.GONE);

        if(getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof AlbumContentsFragment && backPressedCallbackListener !=null){
            mSelectedImagesArray= backPressedCallbackListener.backPressed();
            backPressedCallbackListener = null;
        }
        super.onBackPressed();

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

}
