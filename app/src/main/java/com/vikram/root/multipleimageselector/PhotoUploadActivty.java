package com.vikram.root.multipleimageselector;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;

import android.content.ContentValues;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;


import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
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
import com.vikram.root.multipleimageselector.Utils.ImageUtils;

import java.io.File;
import java.util.ArrayList;


public class PhotoUploadActivty extends FragmentActivity implements AlbumFragment.OnAlbumSelectedListener, View.OnClickListener, ReviewSelectionFragment.AddMoreImagesSelected {

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
    private TextView mAddFromCameraButton;

    private File mNewImageFile;
    private Uri mNewImageUri;


    private static final int REQUEST_IMAGE_CAPTURE = 2001;
    private static final String LAST_BUCKET_ID_KEY = "lastBucketIDKey";
    private static final String NEW_IMAGE_URI_KEY = "newImageURIKey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Log.d("@vikram", "Activity Created");

        setContentView(R.layout.photo_upload_activity);
        if (savedInstanceState == null) {
            setFragment();
        }
        mToolbar = findViewById(R.id.tool_bar);
        mOkButton = (TextView) mToolbar.findViewById(R.id.toolbar_ok);
        mDeleteButton = (ImageView) mToolbar.findViewById(R.id.toolbar_delete);
        mAddFromCameraButton = (TextView) mToolbar.findViewById(R.id.toolbar_camera);
        /*findViewById(R.id.multiselect_on).setOnClickListener(this);*/
        mOkButton.setOnClickListener(this);
        mSelectedImagesArray = new ArrayList<ImageModel>();
        /*mSelectedImages = new HashMap<String,String>();*/
        ImageView mBackImage = (ImageView) mToolbar.findViewById(R.id.back);
        mBackImage.setOnClickListener(this);
        mAddFromCameraButton.setOnClickListener(this);

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
        switch (v.getId()) {
            case R.id.toolbar_ok:
                ReviewSelectionFragment reviewSelection = new ReviewSelectionFragment();
                if (backPressedCallbackListener != null) {
                    mSelectedImagesArray = backPressedCallbackListener.backPressed();
                    /*mSelectedImages = backPressedCallbackListener.backPressed();*/
                }


                reviewSelection.setSelectedImages(mSelectedImagesArray);

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, reviewSelection);
                transaction.addToBackStack(null);
                transaction.commit();
                mAddFromCameraButton.setVisibility(View.GONE);
                break;
            /*case R.id.multiselect_on:
                if(backPressedCallbackListener!=null){
                    backPressedCallbackListener.multiSelectOn();
                }*/
            case R.id.back:
                onBackPressed();
                break;

            case R.id.toolbar_camera:
                takePhotoFromCamera();
                break;
        }
    }


    private void takePhotoFromCamera() {
        mNewImageFile = ImageUtils.getImagepath();
        if (mNewImageFile != null) {
            dispatchTakePictureIntent(Uri.fromFile(mNewImageFile), mNewImageFile.getPath());
        }
    }

    private void dispatchTakePictureIntent(Uri newImageUri, String newImagePath) {

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, getString(R.string.app_name));
        values.put(MediaStore.Images.Media.DATA, newImagePath);
        mNewImageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mNewImageFile));
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void addMoreImagesSelected() {

        AlbumContentsFragment contentsFragment = AlbumContentsFragment.newInstance(mLastBucketId);

        contentsFragment.setSelectedImages(mSelectedImagesArray);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, contentsFragment);
        getSupportFragmentManager().popBackStack();
        backPressedCallbackListener = contentsFragment;
        transaction.commit();

    }


    public interface BackPressedCallback {
        ArrayList<ImageModel> backPressed();
/*
        void multiSelectOn();
*/
    }

    private void setFragment() {

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

        if (newFragment instanceof BackPressedCallback) {
            backPressedCallbackListener = (BackPressedCallback) newFragment;
        }
    }

    @Override
    public void onBackPressed() {

        mDeleteButton.setVisibility(View.GONE);

        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof AlbumContentsFragment && backPressedCallbackListener != null) {
            mSelectedImagesArray = backPressedCallbackListener.backPressed();
            backPressedCallbackListener = null;
        }
        super.onBackPressed();

    }

    private void makeColorTransition(int colorFrom, int colorTo, View onView) {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("@vikram camera", "Result Code : " + resultCode);

        if (resultCode != RESULT_OK) {
            Log.d("@vikram Cmera Rslt nOK ", "blah ");
            if (mNewImageFile != null) {
                Log.d("@vikram file path",mNewImageFile.getPath());
                Log.d("@vikram file uri",mNewImageUri.getPath());
                if (mNewImageFile.getAbsoluteFile().exists()) {
                    if (mNewImageFile.delete()) {
                    } else {
                        Log.d("@vikram file",mNewImageFile.toString());
                        Log.e(getClass().getSimpleName(), "File could not be deleted ");
                    }
                } else {
                    Log.e(getClass().getSimpleName(), "File not present");
                }
                callBroadCastToUpdateLibrary();
            } else {
                Log.e(getClass().getSimpleName(), "File object null");
            }

            return;
        } else {
            Log.d("@vikram Camera Rslt OK ", "Image photoId ");
            galleryAddPic();
            String photoId = mNewImageUri.getLastPathSegment();
            Log.d("@vikram id of clckd",photoId);
            ImageModel clickedImage = new ImageModel(photoId,mNewImageFile.getPath());
            mSelectedImagesArray.add(clickedImage);
            findViewById(R.id.toolbar_ok).performClick();

        }
    }

    public void callBroadCastToUpdateLibrary() {
        if (Build.VERSION.SDK_INT >= 14) {
            Log.e("-->", " >= 14");
            MediaScannerConnection.scanFile(this, new String[]{Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
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
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        if (mNewImageFile != null) {
            File f = new File(mNewImageFile.getPath());
            if (f != null) {
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);

            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {

        outState.putString(LAST_BUCKET_ID_KEY, mLastBucketId);
        outState.putString(NEW_IMAGE_URI_KEY, mNewImageUri.toString());
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Log.d("@vikram", "Photo upload on RestoreInstance");
        if (savedInstanceState != null) {
            Log.d("@vikram", "Photo upload on RestoreInstance, savedInstance is not Null");
            if (savedInstanceState.getString(LAST_BUCKET_ID_KEY) != null) {
                mLastBucketId = savedInstanceState.getString(LAST_BUCKET_ID_KEY);
            }

            if (savedInstanceState.getString(NEW_IMAGE_URI_KEY) != null) {
                mNewImageUri = Uri.parse(savedInstanceState.getString(NEW_IMAGE_URI_KEY));
                mNewImageFile = new File(mNewImageUri.getPath());
            }
        } else {
            Log.d("@vikram", "Photo upload on RestoreInstance, savedInstance is Null");
        }
    }
}
