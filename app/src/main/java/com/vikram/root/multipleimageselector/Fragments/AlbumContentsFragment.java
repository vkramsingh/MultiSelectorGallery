package com.vikram.root.multipleimageselector.Fragments;

import android.database.Cursor;
import android.graphics.Point;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.vikram.root.multipleimageselector.Adapters.ImageCursorAdapter;
import com.vikram.root.multipleimageselector.Constants.Constants;
import com.vikram.root.multipleimageselector.Models.ImageModel;
import com.vikram.root.multipleimageselector.PhotoUploadActivty;
import com.vikram.root.multipleimageselector.R;
import com.vikram.root.multipleimageselector.Utils.ImageUtils;

import java.util.ArrayList;

public class AlbumContentsFragment extends Fragment implements PhotoUploadActivty.UpdateSelectedImageArrayCallback, LoaderManager.LoaderCallbacks<Cursor> {

    final String[] projection = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID, MediaStore.Images.Media.BUCKET_ID};
    final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
    final String selection = MediaStore.Images.Media.BUCKET_ID + "=?";


    private final static String SELECTED_IMAGES_ARRAY_LIST ="selectedImages";


    private int count;
    private GridView mImagesGrid;
    private String[] mBucketId;

    private ArrayList<ImageModel> mSelectedImagesArray;

    private ImageCursorAdapter mImageCursorAdapter;
    private View mToolbar;
    private TextView mToolbarHeading;
    private TextView mOkButton;


    public static AlbumContentsFragment newInstance(String bucketId) {
        AlbumContentsFragment f = new AlbumContentsFragment();
        Bundle args = new Bundle();
        args.putString("bucketId", bucketId);
        f.setArguments(args);
        return f;
    }


    public void setSelectedImages(ArrayList<ImageModel> selectedImages) {
        mSelectedImagesArray = selectedImages;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("@vikram", "On create called");

        mImageCursorAdapter = new ImageCursorAdapter(getActivity(), null, mSelectedImagesArray, null, false);

        mBucketId = new String[1];
        mBucketId[0] = getArguments().getString("bucketId");

        getLoaderManager().initLoader(0, null, this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }


        View rootView = inflater.inflate(R.layout.gallery_layout, container, false);
        mImagesGrid = (GridView) rootView.findViewById(R.id.imageGrid);

        mImagesGrid.setAdapter(mImageCursorAdapter);

        return rootView;
    }

    @Override
    public void onResume() {

        Log.d("@vikram", "Fragment Resumed");

        if (mSelectedImagesArray != null && mSelectedImagesArray.size() != 0) {
            /*mToolbar.setBackgroundColor(getResources().getColor(R.color.photoSelected));*/
            mToolbarHeading.setText(mSelectedImagesArray.size() + " Selected");
            mOkButton.setVisibility(View.VISIBLE);
            /*mToolbar.findViewById(R.id.multiselect_on).setVisibility(View.GONE);*/
        } else {
            if (mSelectedImagesArray != null) {
                mSelectedImagesArray.clear();
            }
            /*mToolbar.setBackgroundColor(getResources().getColor(R.color.normalToolbarColor));*/
            mToolbarHeading.setText("Gallery");
            mOkButton.setVisibility(View.GONE);

            count = 0;
            /*mMultiSelectedButton.setVisibility(View.VISIBLE);*/
        }

        super.onResume();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        if(savedInstanceState !=null && savedInstanceState.getParcelableArrayList(SELECTED_IMAGES_ARRAY_LIST) !=null){
            mSelectedImagesArray=savedInstanceState.getParcelableArrayList(SELECTED_IMAGES_ARRAY_LIST);
        }

        mImageCursorAdapter.setSize(size);

        mToolbar = getActivity().findViewById(R.id.tool_bar);
        mToolbarHeading = (TextView) mToolbar.findViewById(R.id.title);
        mOkButton = (TextView) mToolbar.findViewById(R.id.toolbar_ok);
        /*mMultiSelectedButton = (TextView)mToolbar.findViewById(R.id.multiselect_on);*/


        if (mBucketId[0] != null) {
            if (null != mSelectedImagesArray && mSelectedImagesArray.size() != 0) {
                count = mSelectedImagesArray.size();
                /*makeColorTransition(getResources().getColor(R.color.normalToolbarColor), getResources().getColor(R.color.photoSelected), mToolbar);*/
                mToolbarHeading.setText(count + " Selected");
                mOkButton.setVisibility(View.VISIBLE);
                /*mMultiSelectedButton.setVisibility(View.GONE);*/
            }
        }


        /*mImagesGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if (!mImageCursorAdapter.getMultipleSelectionMode() || count == 0) {
                    Cursor mTempCursor = mImageCursorAdapter.getCursor();
                    mTempCursor.moveToPosition(position);
                    if (ImageUtils.CheckValidImage(mTempCursor.getString(mTempCursor.getColumnIndex(MediaStore.Images.Media.DATA)))) {
                        mImageCursorAdapter.setMultipleSelectionMode(true);
                    *//*makeColorTransition(getResources().getColor(R.color.normalToolbarColor), getResources().getColor(R.color.photoSelected), mToolbar);*//*
                        mToolbarHeading.setText(++count + " Selected");
                        RelativeLayout selectionMask = (RelativeLayout) view.findViewById(R.id.chkBox);
                        selectionMask.setVisibility(View.VISIBLE);
                        mOkButton.setVisibility(View.VISIBLE);
                    *//*mMultiSelectedButton.setVisibility(View.GONE);*//*
                        mSelectedImagesArray.add(new ImageModel(mTempCursor.getString(mTempCursor.getColumnIndex(MediaStore.Images.Media._ID)), mTempCursor.getString(mTempCursor.getColumnIndex(MediaStore.Images.Media.DATA))));
                    } else {
                        Toast.makeText(AlbumContentsFragment.this.getActivity(), Constants.FILE_TYPE_NOT_SUPPORTED_ERROR, Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        });*/

        mImagesGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                               @Override
                                               public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                   Cursor mTempCursor = mImageCursorAdapter.getCursor();
                                                   mTempCursor.moveToPosition(position);
                                                   if (ImageUtils.CheckValidImage(mTempCursor.getString(mTempCursor.getColumnIndex(MediaStore.Images.Media.DATA)))) {
                                                       RelativeLayout selectionMask = (RelativeLayout) view.findViewById(R.id.chkBox);
                                                       boolean isNowSelected = false;
                                                       if (mSelectedImagesArray != null) {
                                                           isNowSelected = !ImageModel.containsId(mSelectedImagesArray, mTempCursor.getString(mTempCursor.getColumnIndex(MediaStore.Images.Media._ID)));
                                                       }
                                                       selectionMask.setVisibility(isNowSelected && count<6 ? View.VISIBLE : View.GONE);
                                                       if (isNowSelected) {
                                                           if(count <6){
                                                               if (count == 0) {
                                                                   mOkButton.setVisibility(View.VISIBLE);

                        /*mMultiSelectedButton.setVisibility(View.GONE);*/
                                                               }
                                                               mToolbarHeading.setText(++count + " Selected");
                                                               String imageFilePath= mTempCursor.getString(mTempCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                                                               mSelectedImagesArray.add(new ImageModel(mTempCursor.getString(mTempCursor.getColumnIndex(MediaStore.Images.Media._ID)),imageFilePath,ImageUtils.getExifOrientation(imageFilePath)));
                                                           }else {
                                                               Toast.makeText(AlbumContentsFragment.this.getActivity(), Constants.FILE_COUNT_LIMIT_REACHED, Toast.LENGTH_SHORT).show();
                                                           }
                                                       } else {
                                                           count--;
                                                           if (mSelectedImagesArray != null && ImageModel.containsId(mSelectedImagesArray, mTempCursor.getString(mTempCursor.getColumnIndex(MediaStore.Images.Media._ID)))) {
                                                               ImageModel.removeObjectWithId(mSelectedImagesArray, mTempCursor.getString(mTempCursor.getColumnIndex(MediaStore.Images.Media._ID)));
                                                           }
                                                       }
                                                       if (count == 0) {
                    /*makeColorTransition(getResources().getColor(R.color.photoSelected), getResources().getColor(R.color.normalToolbarColor), mToolbar);*/
                                                           mToolbarHeading.setText("Gallery");
                                                           mOkButton.setVisibility(View.GONE);
                    /*mMultiSelectedButton.setVisibility(View.VISIBLE);*/
                                                       } else {
                                                           mToolbarHeading.setText(count + " Selected");
                                                       }
                                                   } else {
                                                       Toast.makeText(AlbumContentsFragment.this.getActivity(), Constants.FILE_TYPE_NOT_SUPPORTED_ERROR, Toast.LENGTH_SHORT).show();
                                                   }
                                               }
                                           }
        );
    }




    /*private void makeColorTransition(int colorFrom,int colorTo,View onView ){
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                mToolbar.setBackgroundColor((Integer) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();
    }*/


    @Override
    public ArrayList<ImageModel> updateArray() {
        return mSelectedImagesArray;
    }

    /*@Override
    public void multiSelectOn() {
        mImageCursorAdapter.setMultipleSelectionMode(true);
        *//*makeColorTransition(getResources().getColor(R.color.normalToolbarColor), getResources().getColor(R.color.photoSelected), mToolbar);*//*
        *//*mMultiSelectedButton.setVisibility(View.GONE);*//*
    }*/

    @Override
    public void onPause() {
        super.onPause();
        Log.d("@vikram", "Fragment Paused");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("@vikram", "Fragment Stopped");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("@vikram", "View Destroyed");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("@vikram", "Fragment Destroyed");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("@vikram", "Fragment Detached");
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        Log.d("@vikram", "Create loader called Contents");
        return new CursorLoader(getActivity(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection,
                mBucketId, orderBy + " DESC");
    }

    @Override
    public void onLoadFinished(Loader loader, Cursor data) {
        Log.d("@vikram", "Load finished called Contents");
        mImageCursorAdapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        Log.d("@vikram", "Loader reset called Contents");
        mImageCursorAdapter.changeCursor(null);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(mSelectedImagesArray !=null){
            outState.putParcelableArrayList(SELECTED_IMAGES_ARRAY_LIST,mSelectedImagesArray);
        }
        super.onSaveInstanceState(outState);
    }
}