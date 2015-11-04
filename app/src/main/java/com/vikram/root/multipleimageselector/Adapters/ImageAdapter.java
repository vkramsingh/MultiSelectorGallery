/*
package com.vikram.root.multipleimageselector.Adapters;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.vikram.root.multipleimageselector.R;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {

    private ArrayList<String> mImageList;
    private ArrayList<String> mSelectedImageList;
    private LayoutInflater mInflater;
    private Context mContext;
    private SparseBooleanArray mSparseBooleanArray;
    private int mScreenWidth;
    private ArrayList<String> mSelectedImagesFromIntent;
    private int mScreenHeight;
    private String mBucketName;
    private DisplayImageOptions options;
    private SparseBooleanArray mImageSelectionStatus;
    protected ImageLoader imageLoader;
    private boolean multipleSelectionMode;




    public ImageAdapter(Context context,Point size,String bucketName,ArrayList<String> selectedImages,ArrayList<String> imageList,SparseBooleanArray imageSelectionStatus,ArrayList<String> selectedImagesFromIntent) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mSparseBooleanArray = new SparseBooleanArray();
        this.mImageList = imageList;
        mScreenHeight=size.y;
        mScreenWidth=size.x;
        mBucketName=bucketName;
        mImageSelectionStatus = imageSelectionStatus;
        mSelectedImageList=selectedImages;
        multipleSelectionMode =false;
        imageLoader = ImageLoader.getInstance();
        mSelectedImagesFromIntent=selectedImagesFromIntent;
    }

    public ArrayList<String> getCheckedItems() {
        ArrayList<String> mTempArry = new ArrayList<String>();

        for(int i=0;i< mImageList.size();i++) {
            if(mSparseBooleanArray.get(i)) {
                mTempArry.add(mImageList.get(i));
            }
        }

        return mTempArry;
    }

    @Override
    public int getCount() {
        return mImageList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View view = null;
        final ViewHolder holder;
        if (null != convertView) {
            view = convertView;
            holder = (ViewHolder) view.getTag();
            Log.d("Vikramaa", "reused");
        } else {

            Log.d("Vikramaa","inflated");
            view = View.inflate(mContext, R.layout.gallery_cell, null);
            holder = new ViewHolder();

            holder.cbox = (RelativeLayout)view.findViewById(R.id.chkBox);
            holder.image = (ImageView)view.findViewById(R.id.imageView1);
            holder.image.setLayoutParams(new RelativeLayout.LayoutParams(mScreenWidth / 3, mScreenWidth / 3));
            holder.cbox.setLayoutParams(new RelativeLayout.LayoutParams(mScreenWidth / 3, mScreenWidth / 3));
            view.setTag(holder);
        }

        if(mImageSelectionStatus.indexOfKey(position) < 0){
            if(mSelectedImagesFromIntent !=null && mSelectedImageList.contains(mImageList.get(position))){
                mImageSelectionStatus.put(position,true);
            }
            else{
                mImageSelectionStatus.put(position,false);
            }
        }
        imageLoader.displayImage("file://" + mImageList.get(position), holder.image, options);

        if (multipleSelectionMode) {
            Boolean flag = mImageSelectionStatus.get(position);
            if (flag == null) {
                holder.cbox.setVisibility(View.GONE);
            } else {
                holder.cbox.setVisibility(flag ? View.VISIBLE: View.GONE);
            }
        } else {
            holder.cbox.setVisibility(View.GONE);
        }
        return view;
    }

    class ViewHolder {
        RelativeLayout cbox;
        ImageView image;
    }
    CompoundButton.OnCheckedChangeListener mCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);
        }
    };

    public boolean isMultipleSelectionMode() {
        return multipleSelectionMode;
    }

    public void setMultipleSelectionMode(boolean multipleSelectionMode) {
        this.multipleSelectionMode = multipleSelectionMode;
    }

}*/
