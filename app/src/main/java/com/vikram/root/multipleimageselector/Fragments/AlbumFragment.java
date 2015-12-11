package com.vikram.root.multipleimageselector.Fragments;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;


import com.vikram.root.multipleimageselector.Adapters.ImageCursorAdapter;
import com.vikram.root.multipleimageselector.Models.ImageModel;
import com.vikram.root.multipleimageselector.R;
import com.vikram.root.multipleimageselector.Utils.ImageUtils;

import java.io.File;
import java.util.ArrayList;

public class AlbumFragment extends android.support.v4.app.Fragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {


    private static final String[] PROJECTION_BUCKET = {
            MediaStore.Images.ImageColumns.BUCKET_ID,
            MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
            MediaStore.Images.ImageColumns.DATE_TAKEN,
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.ImageColumns._ID,
            "count()"};


    // We want to order the albums by reverse chronological order. We abuse the
    // "WHERE" parameter to insert a "GROUP BY" clause into the SQL statement.
    // The template for "WHERE" parameter is like:
    //    SELECT ... FROM ... WHERE (%s)
    // and we make it look like:
    //    SELECT ... FROM ... WHERE (1) GROUP BY 1,(2)
    // The "(1)" means true. The "1,(2)" means the first two columns specified
    // after SELECT. Note that because there is a ")" in the template, we use
    // "(2" to match it.
    private static final String BUCKET_GROUP_BY =
            "1) GROUP BY 1,(2";
    private static final String BUCKET_ORDER_BY = "MAX(datetaken) DESC";


    public interface OnAlbumSelectedListener {
        void onAlbumSelected(String bucketId);
    }

    public interface HandleCameraClickedImage {
        void onPhotoRecievedFromCamera(ImageModel clickedImage);
    }

    private OnAlbumSelectedListener mAlbumSelectedListener;
    private HandleCameraClickedImage mCameraClickedImageHandler;
    private GridView mAlbumsGrid;
    private ArrayList<String> mSelectedAlbums;
    private ImageCursorAdapter mAlbumCursorAdapter;
    private ImageButton mCameraButton;

    private File mNewImageFile;
    private Uri mNewImageUri;

    private static final int REQUEST_IMAGE_CAPTURE = 2001;
    private static final String NEW_IMAGE_URI_KEY = "NEW_IMAGE_URI_KEY";

    public static AlbumFragment newInstance(ArrayList<String> mSelectedAlbums) {
        AlbumFragment f = new AlbumFragment();
        Bundle args = new Bundle();
        args.putSerializable("albumList", mSelectedAlbums);
        f.setArguments(args);
        return f;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mSelectedAlbums = (ArrayList<String>) getArguments().getSerializable("albumList");

        getLoaderManager().initLoader(0, null, this);

        if(savedInstanceState !=null){
            if(savedInstanceState.getString(NEW_IMAGE_URI_KEY) !=null){
                mNewImageUri = Uri.parse(savedInstanceState.getString(NEW_IMAGE_URI_KEY));
                mNewImageFile = new File(mNewImageUri.getPath());
            }
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mAlbumSelectedListener = (OnAlbumSelectedListener) activity;
        mCameraClickedImageHandler = (HandleCameraClickedImage) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        View rootView = inflater.inflate(R.layout.albumcontents, container, false);
        mAlbumsGrid = (GridView) rootView.findViewById(R.id.albumGrid);
        mAlbumsGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (view != null) {
                    openAlbum((Cursor) parent.getItemAtPosition(position));
                }
            }
        });

        mCameraButton = (ImageButton) rootView.findViewById(R.id.camera_button);
        mCameraButton.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        /*getActivity().findViewById(R.id.multiselect_on).setVisibility(View.GONE);*/
        mAlbumCursorAdapter = new ImageCursorAdapter(getActivity(), null, null, null, true);
        mAlbumsGrid.setAdapter(mAlbumCursorAdapter);
        mAlbumCursorAdapter.setSize(size);
    }

    public void openAlbum(Cursor mLocalCursor) {
        mAlbumSelectedListener.onAlbumSelected(mLocalCursor.getString(mLocalCursor.getColumnIndex(MediaStore.Images.ImageColumns.BUCKET_ID)));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.camera_button:
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
        mNewImageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mNewImageFile));
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d("@vikram", "On Save Instance State Called Album Fragment");
        if(mNewImageUri !=null){
            outState.putString(NEW_IMAGE_URI_KEY, mNewImageUri.toString());
        }
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.e("@vikram","AlbumFragment Paused");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("@vikram", "AlbumFragment Destroyed");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e("@vikram", "AlbumFragment Detached");
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        Log.d("@vikram", "Photo upload on RestoreInstance fragment");
        if (savedInstanceState != null) {
            Log.d("@vikram", "Photo upload on RestoreInstance, savedInstance is not Null");
            Log.d("@vikram", savedInstanceState.toString());

            if (savedInstanceState.getString(NEW_IMAGE_URI_KEY) != null) {
                mNewImageUri = Uri.parse(savedInstanceState.getString(NEW_IMAGE_URI_KEY));
                mNewImageFile = new File(mNewImageUri.getPath());
            }
        } else {
            Log.d("@vikram", "Photo upload on RestoreInstance, savedInstance is Null");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("@vikram camera", "Result Code : " + resultCode);

        if (resultCode != getActivity().RESULT_OK) {
            Log.d("@vikram Cmera Rslt nOK ", "blah ");
            if (mNewImageFile != null) {
                Log.d("@vikram file path", mNewImageFile.getPath());
                Log.d("@vikram file uri", mNewImageUri.getPath());

                getActivity().getContentResolver().delete(mNewImageUri, null, null);
                if (mNewImageFile.getAbsoluteFile().exists()) {
                    if (mNewImageFile.delete()) {
                    } else {
                        Log.d("@vikram file", mNewImageFile.toString());
                        Log.e(getClass().getSimpleName(), "File could not be deleted ");
                    }
                } else {
                    Log.e(getClass().getSimpleName(), "File not present");
                }
                ImageUtils.callBroadCastToUpdateLibrary(getActivity());
            } else {
                Log.e(getClass().getSimpleName(), "File object null");
            }
            return;
        } else {
            Log.d("@vikram Camera Rslt OK ", "Image photoId ");
            ImageUtils.galleryAddPic(mNewImageFile, getActivity());
            String photoId = mNewImageUri.getLastPathSegment();
            Log.d("@vikram id of clckd", photoId);
            Log.d("@vikram", "orientation is : " + ImageUtils.getExifOrientation(mNewImageFile.getAbsolutePath()));
            ImageModel clickedImage = new ImageModel(photoId, mNewImageFile.getPath(), ImageUtils.getExifOrientation(mNewImageFile.getAbsolutePath()));
            mCameraClickedImageHandler.onPhotoRecievedFromCamera(clickedImage);
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d("@vikram", "Create loader called Album");
        return new CursorLoader(getActivity(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, PROJECTION_BUCKET, BUCKET_GROUP_BY, null, BUCKET_ORDER_BY);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d("@vikram", "Load finished called Album");

        mAlbumCursorAdapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d("@vikram", "Loader reset called Album");

        mAlbumCursorAdapter.changeCursor(null);
    }
}