package com.vikram.root.multipleimageselector.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vikram.root.multipleimageselector.Adapters.ReviewSelectionPagerAdapter;
import com.vikram.root.multipleimageselector.Models.ImageModel;
import com.vikram.root.multipleimageselector.PageTransformers.ZoomOutPageTransformer;
import com.vikram.root.multipleimageselector.R;
import com.vikram.root.multipleimageselector.Utils.ImageUtils;


import java.util.ArrayList;


public class ReviewSelectionFragment extends Fragment implements View.OnClickListener{

    public interface AddMoreImagesSelected{
        void addMoreImagesSelected();
    }


    private AddMoreImagesSelected mAddMoreImagesListener;
    private ArrayList<ImageModel> mSelectedImagesArray;
    /*private HashMap<String,String> mSelectedImagesHash;*/
    /*Iterator mSelectedPhotosHashIterator;*/
    private View mToolbar;
    private TextView mToolbarHeading;
    /*private TextView mMultiSelectButton;*/
    private TextView mOkButton;

    private ImageView mDeleteButton;

    private LinearLayout mImageLayout;

    ReviewSelectionPagerAdapter reviewSelectionPagerAdapter;


    /*private ImageView mImageBig;*/
    private ViewPager mViewPager;



    private int mCurrentPosition = 0;
    private String mCurrentURL;



    public ReviewSelectionFragment newInstance(){
        ReviewSelectionFragment f = new ReviewSelectionFragment();
        return f;
    }


    public void setSelectedImages(ArrayList<ImageModel> selectedImages){
        mSelectedImagesArray=selectedImages;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mAddMoreImagesListener= (AddMoreImagesSelected)activity;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mToolbar =  getActivity().findViewById(R.id.tool_bar);
        mToolbarHeading = (TextView) mToolbar.findViewById(R.id.title);
        mOkButton = (TextView) mToolbar.findViewById(R.id.toolbar_ok);
        mDeleteButton = (ImageView) mToolbar.findViewById(R.id.toolbar_delete);
        reviewSelectionPagerAdapter = new ReviewSelectionPagerAdapter(getActivity(),mSelectedImagesArray);
        mViewPager.setAdapter(reviewSelectionPagerAdapter);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mImageLayout.getChildAt(mCurrentPosition).findViewById(R.id.image_thumbnail_imageview).setBackgroundResource((android.R.color.transparent));
                mCurrentPosition=position;
                mCurrentURL=mSelectedImagesArray.get(position).getPath();
                mImageLayout.getChildAt(position).findViewById(R.id.image_thumbnail_imageview).setBackgroundResource(R.drawable.review_thumbnail_selected_border);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(container == null){
            return null;
        }
        View rootView= inflater.inflate(R.layout.review_selection,container,false);

        /*mImageBig = (ImageView) rootView.findViewById(R.id.imageBig);*/

        mViewPager = (ViewPager)rootView.findViewById(R.id.imageAndTagPager);

        mImageLayout=(LinearLayout)rootView.findViewById(R.id.imageLayout);

        if(mSelectedImagesArray !=null){
            String firstImage = mSelectedImagesArray.get(0).getPath();
            /*mImageBig.setImageBitmap(ImageUtils.decodeSampledBitmapFromFile(firstImage, mImageBig.getLayoutParams().width, mImageBig.getLayoutParams().height));*/
            mCurrentURL = firstImage;
            if(mSelectedImagesArray.size()<=6){
                int index=0;
                for(int i=0;i<mSelectedImagesArray.size();i++){
                    View mThumbnail = inflater.inflate(R.layout.image_thumbnail,null,false);
                    String mPhotoPath = mSelectedImagesArray.get(i).getPath();
                    ImageView mImageView=(ImageView)mThumbnail.findViewById(R.id.image_thumbnail_imageview);
                    mImageView.setImageBitmap(ImageUtils.decodeSampledBitmapFromFile(mPhotoPath, mImageView.getLayoutParams().width, mImageView.getLayoutParams().height));
                    mImageLayout.addView(mThumbnail);
                    mImageView.setOnClickListener(this);
                    mImageView.setTag(mPhotoPath);
                    mImageView.setTag(R.string.index,i);
                }
                if(mImageLayout.getChildAt(0) !=null){
                    mImageLayout.getChildAt(0).findViewById(R.id.image_thumbnail_imageview).setBackgroundResource(R.drawable.review_thumbnail_selected_border);
                }
                View mAddThumbnail=inflater.inflate(R.layout.image_add,null,false);
                mImageLayout.addView(mAddThumbnail);
                rootView.findViewById(R.id.image_thumbnail_add_photos).setOnClickListener(this);
            }

        }

        /*rootView.findViewById(R.id.photo_tag).setOnClickListener(this);*/

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        /*mToolbar.setBackgroundColor(getResources().getColor(R.color.normalToolbarColor));*/
        mOkButton.setVisibility(View.GONE);
        /*mMultiSelectButton.setVisibility(View.GONE);*/
        mToolbarHeading.setText("Review Selection");

        mDeleteButton.setVisibility(View.VISIBLE);
        mDeleteButton.setOnClickListener(this);
    }





    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_thumbnail_imageview:
                mCurrentURL =(String)v.getTag() ;
                Log.d("@vikram Review Select", "Position of view is : " + ((ViewGroup)v.getParent()).indexOfChild(v));
                Log.d("@vikram Review Select", "Children count is "+mImageLayout.getChildCount());
                /*mImageBig.setImageBitmap(ImageUtils.decodeSampledBitmapFromFile(mCurrentURL, mImageBig.getLayoutParams().width, mImageBig.getLayoutParams().height));*/
                mViewPager.setCurrentItem((int) v.getTag(R.string.index));
                break;
            case R.id.image_thumbnail_add_photos:
                getActivity().onBackPressed();
                break;
            case R.id.photo_tag:
                getBasicDialog(getActivity(), "Select Tag").show();
                break;
            case R.id.toolbar_delete:
                int newPosition=mCurrentPosition;
                if(mSelectedImagesArray.size()==1){
                    ImageModel.removeObjectWithPath(mSelectedImagesArray, mCurrentURL);
                    reviewSelectionPagerAdapter.notifyDataSetChanged();
                    getActivity().onBackPressed();
                } else if(mCurrentPosition >= 0 ){
                    String newImageURL;
                    if(mCurrentPosition == mSelectedImagesArray.size()-1){
                        newImageURL=(String)mImageLayout.getChildAt(mCurrentPosition -1).findViewById(R.id.image_thumbnail_imageview).getTag();
                        mViewPager.setAdapter(null);
                        newPosition--;
                    } else {
                        newImageURL=(String)mImageLayout.getChildAt(mCurrentPosition +1).findViewById(R.id.image_thumbnail_imageview).getTag();
                    }
                    ImageModel.removeObjectWithPath(mSelectedImagesArray, mCurrentURL);
                    /*mImageBig.setImageBitmap(ImageUtils.decodeSampledBitmapFromFile(newImageURL, mImageBig.getLayoutParams().width, mImageBig.getLayoutParams().height));*/
                    mCurrentURL=newImageURL;

                }
                mImageLayout.removeViewAt(mCurrentPosition);
                reviewSelectionPagerAdapter.notifyDataSetChanged();
                mViewPager.setAdapter(reviewSelectionPagerAdapter);
                updateIndexTags(mCurrentPosition);
                mCurrentPosition=newPosition;
                mViewPager.setCurrentItem(mCurrentPosition);
                break;
        }
    }

    public void updateIndexTags(int position){
        int childCount=mImageLayout.getChildCount();
        for(int i=position;i<childCount-1;i++){
            View thumbnailView = mImageLayout.getChildAt(i).findViewById(R.id.image_thumbnail_imageview);
            int indexSaved=(int)thumbnailView.getTag(R.string.index);
            thumbnailView.setTag(R.string.index,indexSaved-1);
        }
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
}