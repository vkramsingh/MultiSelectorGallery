package com.vikram.root.multipleimageselector;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.vikram.root.multipleimageselector.Fragments.AlbumContentsFragment;
import com.vikram.root.multipleimageselector.Fragments.AlbumFragment;
import com.vikram.root.multipleimageselector.Fragments.ReviewSelectionFragment;
import com.vikram.root.multipleimageselector.Models.ImageModel;

import java.util.ArrayList;


public class PhotoUploadActivty extends FragmentActivity implements AlbumFragment.OnAlbumSelectedListener, View.OnClickListener, ReviewSelectionFragment.AddMoreImagesSelected, AlbumFragment.HandleCameraClickedImage {

    private ArrayList<String> mSelectedAlbums;
    public ArrayList<ImageModel> mSelectedImagesArray;
    /*
        public HashMap<String,String> mSelectedImages;
    */
    private UpdateSelectedImageArrayCallback selectedImagesArrayListener;
    private String mLastBucketId;
    private View mToolbar;
    private TextView mOkButton;
    private ImageView mDeleteButton;
    private TextView mToolbarHeading;


    public static final String KEY_ARRAYLIST_IMAGE = "KEY_ARRAYLIST_IMAGE";
    private static final String LAST_BUCKET_ID_KEY = "lastBucketIDKey";
    public static final String REQUEST_FOR = "REQUEST_FOR";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Log.d("@vikram", "Photo Upload Activity Created");

        setContentView(R.layout.photo_upload_activity);

        mToolbar = findViewById(R.id.tool_bar);
        mOkButton = (TextView) mToolbar.findViewById(R.id.toolbar_ok);
        mDeleteButton = (ImageView) mToolbar.findViewById(R.id.toolbar_delete);
        mToolbarHeading = (TextView) mToolbar.findViewById(R.id.title);
        /*findViewById(R.id.multiselect_on).setOnClickListener(this);*/
        mOkButton.setOnClickListener(this);
        mSelectedImagesArray = new ArrayList<ImageModel>();
        /*mSelectedImages = new HashMap<String,String>();*/
        ImageView mBackImage = (ImageView) mToolbar.findViewById(R.id.back);
        mBackImage.setOnClickListener(this);
        if (getIntent().getSerializableExtra(KEY_ARRAYLIST_IMAGE) != null) {
            mSelectedImagesArray = (ArrayList<ImageModel>) getIntent().getSerializableExtra(KEY_ARRAYLIST_IMAGE);
            if (mSelectedImagesArray.size() > 0) {
                mOkButton.setVisibility(View.VISIBLE);
                ((TextView) mToolbar.findViewById(R.id.title)).setText(mSelectedImagesArray.size() + " Selected");
            }
        }


        if (getIntent().getStringExtra(REQUEST_FOR) != null && getIntent().getStringExtra(REQUEST_FOR).equals("REVIEW_SELECTION")) {
            showReviewSelection(true);
        } else if (savedInstanceState == null) {
            showAlbumFragment();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        //Log.d("@vikram", "Activity Started");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Log.d("@vikram", "Activity resumed");
    }

    @Override
    protected void onPause() {
        super.onPause();

        //Log.d("@vikram", "Activity Paused");
    }

    @Override
    protected void onStop() {
        super.onStop();

        //Log.d("@vikram", "Activity Stopped");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        //Log.d("@vikram", "Activity Restarted");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Log.d("@vikram", "Activity Destroyed");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_ok:
                if (selectedImagesArrayListener != null) {
                    mSelectedImagesArray = selectedImagesArrayListener.updateArray();
                    /*mSelectedImages = selectedImagesArrayListener.updateArray();*/
                }
                showReviewSelection(false);
                break;
            /*case R.id.multiselect_on:
                if(selectedImagesArrayListener!=null){
                    selectedImagesArrayListener.multiSelectOn();
                }*/
            case R.id.back:
                onBackPressed();
                break;
        }
    }

    private void showReviewSelection(boolean directRequest) {
        ReviewSelectionFragment reviewSelection = new ReviewSelectionFragment();
        reviewSelection.setSelectedImages(mSelectedImagesArray);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, reviewSelection);
        if (!directRequest) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }


    @Override
    public void addMoreImagesSelected(ArrayList<ImageModel> updatedSelectedImagesArray) {

        mSelectedImagesArray = updatedSelectedImagesArray;
        showAlbumFragment();
    }

    @Override
    public void onPhotoRecievedFromCamera(ImageModel clickedImage) {
        mSelectedImagesArray.add(clickedImage);
        findViewById(R.id.toolbar_ok).performClick();
    }


    public interface UpdateSelectedImageArrayCallback {
        ArrayList<ImageModel> updateArray();
/*
        void multiSelectOn();
*/
    }

    private void showAlbumFragment() {


        mDeleteButton.setVisibility(View.GONE);
        if (mSelectedImagesArray != null && mSelectedImagesArray.size() > 0) {
            mToolbarHeading.setText(mSelectedImagesArray.size() + " Selected");
        }
        AlbumFragment albumFragment = AlbumFragment.newInstance(mSelectedAlbums);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();


        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) != null) {
            transaction.replace(R.id.fragment_container, albumFragment);
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        } else {
            transaction.add(R.id.fragment_container, albumFragment);
        }

        transaction.commit();
    }


    @Override
    public void onAlbumSelected(String bucketId) {

        mLastBucketId = bucketId;

        AlbumContentsFragment newAlbumContentsFragment = AlbumContentsFragment.newInstance(bucketId);
        newAlbumContentsFragment.setSelectedImages(mSelectedImagesArray);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();


        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, newAlbumContentsFragment);
        transaction.addToBackStack(null);
        // Commit the transaction
        transaction.commit();

        if (newAlbumContentsFragment instanceof UpdateSelectedImageArrayCallback) {
            selectedImagesArrayListener = (UpdateSelectedImageArrayCallback) newAlbumContentsFragment;
        }
    }

    @Override
    public void onBackPressed() {

        mDeleteButton.setVisibility(View.GONE);

        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof AlbumContentsFragment && selectedImagesArrayListener != null) {
            mSelectedImagesArray = selectedImagesArrayListener.updateArray();
            selectedImagesArrayListener = null;
        }
        super.onBackPressed();

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        //Log.d("@vikram", "On Save Instance State Called");
        if (mLastBucketId != null) {
            outState.putString(LAST_BUCKET_ID_KEY, mLastBucketId);
        }
        /*outState.putString(NEW_IMAGE_URI_KEY, mNewImageUri.toString());*/
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        //Log.d("@vikram", "Photo upload on RestoreInstance photo upload activity");
        if (savedInstanceState != null) {
            //Log.d("@vikram", "Photo upload on RestoreInstance photo upload activity, savedInstance is not Null");
            //Log.d("@vikram", savedInstanceState.toString());
            if (savedInstanceState.getString(LAST_BUCKET_ID_KEY) != null) {
                mLastBucketId = savedInstanceState.getString(LAST_BUCKET_ID_KEY);
            }
        } else {
            //Log.d("@vikram", "Photo upload on RestoreInstance, savedInstance is Null");
        }
    }
}